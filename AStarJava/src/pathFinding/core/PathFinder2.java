package pathFinding.core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import pathFinding.heuristics.AStarHeuristic;
import pathFinding.heuristics.DiagonalHeuristic;
import pathFinding.utils.Logger;
import pathFinding.utils.StopWatch;
import pathFinding.utils.Vector2D;

public class PathFinder2 {
	private static final float FIX_DISTANCE = 0.01f;
	
	private AStarMap map;
	private Logger log = new Logger();
	private StopWatch s = new StopWatch();
	
	public PathFinder2(AStarMap map) {
		this.map = map;
	}
	
	public static Point posToCell(Vector2D pos, float cellSize) {
		int cellX = (int)Math.floor(pos.x / cellSize);
		int cellY = (int)Math.floor(pos.y / cellSize);
		return new Point(cellX, cellY);
	}
	
	public static Vector2D cellToPos(Point cell, float cellSize) {
		float x = (cell.x + 0.5f) * cellSize;
		float y = (cell.y + 0.5f) * cellSize;
		return new Vector2D(x, y);
	}
	
	public List<Vector2D> findStraightPath(Vector2D start, Vector2D goal, float cellSize) {
		Point startCell = posToCell(start, cellSize);
		Point goalCell = posToCell(goal, cellSize);
		
		// optimized, check can straight pass
		Point hitPoint = raycast(start, goal, cellSize);
		if(hitPoint.equals(goalCell)) {
			List<Vector2D> waypoints = new ArrayList<>();
			waypoints.add(start);
			waypoints.add(goal);
			return waypoints;
		}
		
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
		} else {
			Vector2D pos1 = cellToPos(shortestPath.get(0), cellSize);
			for(int i = 1; i < shortestPath.size(); ++i) {
				Vector2D pos2 = cellToPos(shortestPath.get(i), cellSize);
				Vector2D dir = pos2.sub(pos1).normalize();
				float midx = (pos1.x + pos2.x) / 2;
				float midy = (pos1.y + pos2.y) / 2;
				posPath.add(new Vector2D(midx - 0.1f * dir.x, midy - 0.1f * dir.y));
				posPath.add(new Vector2D(midx + 0.1f * dir.x, midy + 0.1f * dir.y));
				
				pos1 = pos2;
			}
			
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
	
	private static Vector2D getNextPos(Vector2D currPos, Vector2D dir, float cellSize) {
		float nextx = 0;
		float nexty = 0;
		
		Point coord = posToCell(currPos, cellSize);
		float dx;
		float dy;
		if(dir.x > 0) {
			if(dir.y > 0) {
				float cx = (coord.x + 1) * cellSize;
				float cy = (coord.y + 1) * cellSize;
				dx = cx - currPos.x;
				dy = cy - currPos.y;
			} else {
				float cx = (coord.x + 1) * cellSize;
				float cy = coord.y * cellSize;
				dx = cx - currPos.x;
				dy = currPos.y - cy;
			}
		} else {
			if(dir.y > 0) {
				float cx = coord.x * cellSize;
				float cy = (coord.y + 1) * cellSize;
				dx = currPos.x - cx;
				dy = cy - currPos.y;
			} else {
				float cx = coord.x * cellSize;
				float cy = coord.y * cellSize;
				dx = currPos.x - cx;
				dy = currPos.y - cy;
			}
		}
		
		float factorx = Math.abs(dx / dir.x);
		float factory = Math.abs(dy / dir.y);
		
		if(Float.isInfinite(factorx)) {
			factorx = 0.0f;
		}
		
		if(Float.isInfinite(factory)) {
			factory = 0.0f;
		}
		
		if(factory == 0.0f) {
			nextx = currPos.x + dir.x * factorx;
			nexty = currPos.y + dir.y * factorx;
		} else if(factorx == 0.0f) {
			nextx = currPos.x + dir.x * factory;
			nexty = currPos.y + dir.y * factory;
		} else if(factorx < factory) {
			nextx = currPos.x + dir.x * factorx;
			nexty = currPos.y + dir.y * factorx;
		} else {
			nextx = currPos.x + dir.x * factory;
			nexty = currPos.y + dir.y * factory;
		}
		
		Vector2D nextPos = new Vector2D(nextx, nexty);
		
		Point newCell;
		do{
			nextPos.x += dir.x * FIX_DISTANCE;
			nextPos.y += dir.y * FIX_DISTANCE;
			newCell = posToCell(nextPos, cellSize);
		}while(newCell.equals(coord));
		
		return nextPos;
	}
	
	public static Vector2D getNextPosBeforeNextCell(Vector2D currPos, Vector2D dir, float cellSize) {
		Vector2D nextPos = getNextPos(currPos, dir, cellSize);
		nextPos.x -= dir.x * FIX_DISTANCE * 2;
		nextPos.y -= dir.y * FIX_DISTANCE * 2;
		return nextPos;
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
		
		float diffx = goalPos.x - startPos.x;
		float diffy = goalPos.y - startPos.y;
		float distanceSquare = diffx * diffx + diffy * diffy;
		float distance = (float)Math.sqrt(distanceSquare);
		
		Vector2D dir = new Vector2D(diffx / distance, diffy / distance);
		
		Vector2D lastPassed = startPos;
		Point hitPoint = new Point(startCell);
		while(true) {
			Vector2D nextPos = getNextPos(lastPassed, dir, cellSize);
			
			Point newCell = posToCell(nextPos, cellSize);
			
			float dis = (nextPos.x-startPos.x) * (nextPos.x-startPos.x) + (nextPos.y-startPos.y) * (nextPos.y-startPos.y);
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
			
			lastPassed = nextPos;
		}
				
		return hitPoint;
	}
}
