package pathFinding.core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import pathFinding.heuristics.AStarHeuristic;
import pathFinding.heuristics.DiagonalHeuristic;
import pathFinding.utils.Logger;
import pathFinding.utils.StopWatch;
import pathFinding.utils.Vector2D;

public class PathFinder {
	private AStarMap map;
	private Logger log = new Logger();
	private StopWatch s = new StopWatch();
	
	public PathFinder(AStarMap map) {
		this.map = map;
	}
	
	private static Point toPoint(float x, float y) {
		return new Point((int)Math.floor(x), (int)Math.floor(y));
	}
	
	/**
	 * Bresenham line algorithm
	 * @param start
	 * @param goal
	 * @return
	 */
	public Point raycast(Point start, Point goal) {
		// exception: start is obstacle. Now just return start
		if(AStarCell.isObstacle(map.getCell(start.x, start.y))) {
			return start;
		}
		
		if(start.equals(goal)) {
			return start;
		}
		
		List<Point> line = new ArrayList<Point>();
		line.add(start);
		
		float startx = start.x + 0.5f;
		float starty = start.y + 0.5f;
		float goalx = goal.x + 0.5f;
		float goaly = goal.y + 0.5f;
		
		float diffx = goalx - startx;
		float diffy = goaly - starty;
		float distanceSquare = diffx * diffx + diffy * diffy;
		float distance = (float)Math.sqrt(distanceSquare);
		
		float dirx = diffx / distance;
		float diry = diffy / distance;
		
		float lastPassedx = startx;
		float lastPassedy = starty;
		Point hitPoint = new Point(start);
		while(true) {
			float nextx = 0;
			float nexty = 0;
			
			Point coord = toPoint(lastPassedx, lastPassedy);
			float dx;
			float dy;
			if(dirx > 0) {
				if(diry > 0) {
					float cx = coord.x + 1;
					float cy = coord.y + 1;
					dx = cx - lastPassedx;
					dy = cy - lastPassedy;
				} else {
					float cx = coord.x + 1;
					float cy = coord.y;
					dx = cx - lastPassedx;
					dy = lastPassedy - cy;
				}
			} else {
				if(diry > 0) {
					float cx = coord.x;
					float cy = coord.y + 1;
					dx = lastPassedx - cx;
					dy = cy - lastPassedy;
				} else {
					float cx = coord.x;
					float cy = coord.y;
					dx = lastPassedx - cx;
					dy = lastPassedy - cy;
				}
			}
			
			float factorx = Math.abs(dx / dirx);
			float factory = Math.abs(dy / diry);
			
			if(Float.isInfinite(factorx)) {
				factorx = 0.0f;
			}
			
			if(Float.isInfinite(factory)) {
				factory = 0.0f;
			}
			
			if(factory == 0.0f) {
				nextx = lastPassedx + dirx * factorx;
				nexty = lastPassedy + diry * factorx;
			} else if(factorx == 0.0f) {
				nextx = lastPassedx + dirx * factory;
				nexty = lastPassedy + diry * factory;
			} else if(factorx < factory) {
				nextx = lastPassedx + dirx * factorx;
				nexty = lastPassedy + diry * factorx;
			} else {
				nextx = lastPassedx + dirx * factory;
				nexty = lastPassedy + diry * factory;
			}
			
			Point newCell;
			do{
				nextx += dirx * 0.001f;
				nexty += diry * 0.001f;
				newCell = toPoint(nextx, nexty);
			}while(newCell.equals(coord));
			
			float dis = (nextx-startx) * (nextx-startx) + (nexty-starty) * (nexty-starty);
			if(dis >= distanceSquare) {
				break;
			}
			
			if(AStarCell.isObstacle(map.getCell(newCell.x, newCell.y))) {
				break;
			} else {
				hitPoint.x = newCell.x;
				hitPoint.y = newCell.y;
				
				if(newCell.equals(goal)) {
					break;
				}
			}
			
			lastPassedx = nextx;
			lastPassedy = nexty;
		}
				
		return hitPoint;
	}
	
	public List<Point> findStraightPath(Point start, Point goal) {
		// optimized, check can straight pass
		Point hitPoint = raycast(start, goal);
		if(hitPoint.equals(goal)) {
			List<Point> waypoints = new ArrayList<Point>();
			waypoints.add(start);
			waypoints.add(goal);
			return waypoints;
		}
		
		log.addToLog("AStar Heuristic initializing...");
		AStarHeuristic heuristic = new DiagonalHeuristic();
		
		log.addToLog("AStar initializing...");
		AStar aStar = new AStar(map, heuristic);
		
		log.addToLog("Calculating shortest path with AStar...");
		List<Point> shortestPath = aStar.calcShortestPath(start.x, start.y, goal.x, goal.y);
		if(shortestPath == null || shortestPath.isEmpty()) {
			return null;
		}
		
		log.addToLog("Calculating optimized waypoints...");
		s.start();
		List<Point> waypoints = calcStraightPath(shortestPath);
		s.stop();
		log.addToLog("Time to calculate waypoints: " + s.getElapsedTimeUSecs() + " us");
		
		return waypoints;
	}
	
	public List<Point> calcStraightPath(List<Point> shortestPath) {
		if(shortestPath == null || shortestPath.size() <= 2) {
			return shortestPath;
		}
		
		List<Point> waypoints = new ArrayList<Point>();
		
		Point p1 = shortestPath.get(0);
		int p1Number = 0;
		waypoints.add(p1);
		
		int p2Number = 1;
		do{
			Point p2 = shortestPath.get(p2Number);
			if(!lineClear(p1, p2)) {
				p1Number = p2Number-1;
				p1 = shortestPath.get(p1Number);
				waypoints.add(p1);
				log.addToLog("Got waypoint: " + p1.toString());
			}
			p2Number++;
		}while(p2Number < shortestPath.size());
		waypoints.add(shortestPath.get(p2Number - 1));
		
		return waypoints;
	}
	
	private boolean lineClear(Point a, Point b) {
		Point hitPoint = raycast(a, b);
		if(hitPoint.equals(b)) {
			return true;
		} else {
			return false;
		}
	}
	
	//-------------------------------------------------------------------------------------------
	
	public static Point posToCell(Vector2D pos, float cellSize) {
		int cellX = (int)Math.floor(pos.x / cellSize);
		int cellY = (int)Math.floor(pos.y / cellSize);
		return new Point(cellX, cellY);
	}
	
	public static Point posToCell(float posx, float posy, float cellSize) {
		int cellX = (int)Math.floor(posx / cellSize);
		int cellY = (int)Math.floor(posy / cellSize);
		return new Point(cellX, cellY);
	}
	
	public static Vector2D cellToPos(Point cell, float cellSize) {
		float x = (cell.x + 0.5f) * cellSize;
		float y = (cell.y + 0.5f) * cellSize;
		return new Vector2D(x, y);
	}
	
	public List<Vector2D> findStraightPath(Vector2D start, Vector2D goal, float cellSize) {
		// optimized, check can straight pass
//		Point hitPoint = raycast(start, goal);
//		if(hitPoint.equals(goal)) {
//			List<Point> waypoints = new ArrayList<Point>();
//			waypoints.add(start);
//			waypoints.add(goal);
//			return waypoints;
//		}
		
		Point startCell = posToCell(start, cellSize);
		Point goalCell = posToCell(goal, cellSize);
		
		log.addToLog("AStar Heuristic initializing...");
		AStarHeuristic heuristic = new DiagonalHeuristic();
		
		log.addToLog("AStar initializing...");
		AStar aStar = new AStar(map, heuristic);
		
		log.addToLog("Calculating shortest path with AStar...");
		List<Point> shortestPath = aStar.calcShortestPath(startCell.x, startCell.y, goalCell.x, goalCell.y);
		
		// 终点不可达 
		if(shortestPath == null || shortestPath.isEmpty()) {
			return null;
		}
		
		List<Vector2D> posPath = new ArrayList<>();
		posPath.add(start);
		
		// 起点和终点在同一个cell
		if(shortestPath.size() == 1) {
			posPath.add(goal);
			return posPath;
		// 起点和终点在相邻的cell，起始cell和结束cell修正成一个中间点
		} else if(shortestPath.size() == 2) {
			assert startCell.equals(shortestPath.get(0));
			assert goalCell.equals(shortestPath.get(1));
			
			Vector2D pos1 = cellToPos(startCell, cellSize);
			Vector2D pos2 = cellToPos(goalCell, cellSize);
			Vector2D dir = pos2.sub(pos1).normalize();
			float midx = (pos1.x + pos2.x) / 2;
			float midy = (pos1.y + pos2.y) / 2;
			posPath.add(new Vector2D(midx - 0.01f * dir.x, midy - 0.01f * dir.y));
			posPath.add(new Vector2D(midx + 0.01f * dir.x, midy + 0.01f * dir.y));
			
			posPath.add(goal);
		// 否则，起始cell和结束cell各修正为中间点
		} else {
			Vector2D pos1 = cellToPos(shortestPath.get(0), cellSize);
			Vector2D pos2 = cellToPos(shortestPath.get(1), cellSize);
			Vector2D dir = pos2.sub(pos1).normalize();
			float midx = (pos1.x + pos2.x) / 2;
			float midy = (pos1.y + pos2.y) / 2;
			posPath.add(new Vector2D(midx - 0.01f * dir.x, midy - 0.01f * dir.y));
			posPath.add(new Vector2D(midx + 0.01f * dir.x, midy + 0.01f * dir.y));
			
			for(int i = 1; i < shortestPath.size() - 1; ++i) {
				posPath.add(cellToPos(shortestPath.get(i), cellSize));
			}
			
			pos1 = cellToPos(shortestPath.get(shortestPath.size() - 2), cellSize);
			pos2 = cellToPos(shortestPath.get(shortestPath.size() - 1), cellSize);
			dir = pos2.sub(pos1).normalize();
			midx = (pos1.x + pos2.x) / 2;
			midy = (pos1.y + pos2.y) / 2;
			posPath.add(new Vector2D(midx - 0.01f * dir.x, midy - 0.01f * dir.y));
			posPath.add(new Vector2D(midx + 0.01f * dir.x, midy + 0.01f * dir.y));
			
			posPath.add(goal);
		}
		
		
		log.addToLog("Calculating optimized waypoints...");
		s.start();
		posPath = calcStraightPath(posPath, cellSize);
		s.stop();
		log.addToLog("Time to calculate waypoints: " + s.getElapsedTimeUSecs() + " us");
		
		return posPath;
	}
	
	public List<Vector2D> calcStraightPath(List<Vector2D> shortestPath, float cellSize) {
		if(shortestPath == null || shortestPath.size() <= 2) {
			return shortestPath;
		}
		
		List<Vector2D> waypoints = new ArrayList<Vector2D>();
		
		Vector2D p1 = shortestPath.get(0);
		int p1Number = 0;
		waypoints.add(p1);
		
		int p2Number = 1;
		do{
			Vector2D p2 = shortestPath.get(p2Number);
			if(!lineClear(p1, p2, cellSize)) {
				p1Number = p2Number-1;
				p1 = shortestPath.get(p1Number);
				waypoints.add(p1);
				log.addToLog("Got waypoint: " + p1.toString());
			}
			p2Number++;
		}while(p2Number < shortestPath.size());
		waypoints.add(shortestPath.get(p2Number - 1));
		
		return waypoints;
	}
	
	private boolean lineClear(Vector2D startPos, Vector2D goalPos, float cellSize) {
		Point hitPoint = raycast(startPos, goalPos, cellSize);
		if(hitPoint.equals(posToCell(goalPos, cellSize))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Bresenham line algorithm
	 * @param start
	 * @param goal
	 * @return
	 */
	public Point raycast(Vector2D startPos, Vector2D goalPos, float cellSize) {
		Point startCell = posToCell(startPos, cellSize);
		Point goalCell = posToCell(goalPos, cellSize);
		
		// exception: start is obstacle. Now just return start
		if(AStarCell.isObstacle(map.getCell(startCell.x, startCell.y))) {
			return startCell;
		}
		
		if(startCell.equals(goalCell)) {
			return startCell;
		}
		
		float startx = startPos.x;
		float starty = startPos.y;
		float goalx = goalPos.x;
		float goaly = goalPos.y;
		
		float diffx = goalx - startx;
		float diffy = goaly - starty;
		float distanceSquare = diffx * diffx + diffy * diffy;
		float distance = (float)Math.sqrt(distanceSquare);
		
		float dirx = diffx / distance;
		float diry = diffy / distance;
		
		float lastPassedx = startx;
		float lastPassedy = starty;
		Point hitPoint = new Point(startCell);
		while(true) {
			float nextx = 0;
			float nexty = 0;
			
			Point coord = posToCell(lastPassedx, lastPassedy, cellSize);
			float dx;
			float dy;
			if(dirx > 0) {
				if(diry > 0) {
					float cx = (coord.x + 1) * cellSize;
					float cy = (coord.y + 1) * cellSize;
					dx = cx - lastPassedx;
					dy = cy - lastPassedy;
				} else {
					float cx = (coord.x + 1) * cellSize;
					float cy = coord.y * cellSize;
					dx = cx - lastPassedx;
					dy = lastPassedy - cy;
				}
			} else {
				if(diry > 0) {
					float cx = coord.x * cellSize;
					float cy = (coord.y + 1) * cellSize;
					dx = lastPassedx - cx;
					dy = cy - lastPassedy;
				} else {
					float cx = coord.x * cellSize;
					float cy = coord.y * cellSize;
					dx = lastPassedx - cx;
					dy = lastPassedy - cy;
				}
			}
			
			float factorx = Math.abs(dx / dirx);
			float factory = Math.abs(dy / diry);
			
			if(Float.isInfinite(factorx)) {
				factorx = 0.0f;
			}
			
			if(Float.isInfinite(factory)) {
				factory = 0.0f;
			}
			
			if(factory == 0.0f) {
				nextx = lastPassedx + dirx * factorx;
				nexty = lastPassedy + diry * factorx;
			} else if(factorx == 0.0f) {
				nextx = lastPassedx + dirx * factory;
				nexty = lastPassedy + diry * factory;
			} else if(factorx < factory) {
				nextx = lastPassedx + dirx * factorx;
				nexty = lastPassedy + diry * factorx;
			} else {
				nextx = lastPassedx + dirx * factory;
				nexty = lastPassedy + diry * factory;
			}
			
			Point newCell;
			do{
				nextx += dirx * 0.001f;
				nexty += diry * 0.001f;
				newCell = posToCell(nextx, nexty, cellSize);
			}while(newCell.equals(coord));
			
			float dis = (nextx-startx) * (nextx-startx) + (nexty-starty) * (nexty-starty);
			if(dis >= distanceSquare) {
				break;
			}
			
			if(AStarCell.isObstacle(map.getCell(newCell.x, newCell.y))) {
				break;
			} else {
				hitPoint.x = newCell.x;
				hitPoint.y = newCell.y;
				
				if(newCell.equals(goalCell)) {
					break;
				}
			}
			
			lastPassedx = nextx;
			lastPassedy = nexty;
		}
				
		return hitPoint;
	}
}
