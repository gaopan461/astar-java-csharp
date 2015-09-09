package pathFinding.core;

import java.awt.Point;
import java.util.ArrayList;

import pathFinding.heuristics.AStarHeuristic;
import pathFinding.heuristics.DiagonalHeuristic;
import pathFinding.utils.Logger;
import pathFinding.utils.StopWatch;

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
		
		ArrayList<Point> line = new ArrayList<Point>();
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
	
	public ArrayList<Point> findStraightPath(Point start, Point goal) {
		// optimized, check can straight pass
		Point hitPoint = raycast(start, goal);
		if(hitPoint.equals(goal)) {
			ArrayList<Point> waypoints = new ArrayList<Point>();
			waypoints.add(start);
			waypoints.add(goal);
			return waypoints;
		}
		
		log.addToLog("AStar Heuristic initializing...");
		AStarHeuristic heuristic = new DiagonalHeuristic();
		
		log.addToLog("AStar initializing...");
		AStar aStar = new AStar(map, heuristic);
		
		log.addToLog("Calculating shortest path with AStar...");
		ArrayList<Point> shortestPath = aStar.calcShortestPath(start.x, start.y, goal.x, goal.y);
		if(shortestPath == null || shortestPath.isEmpty()) {
			return null;
		}
		
		log.addToLog("Calculating optimized waypoints...");
		s.start();
		ArrayList<Point> waypoints = calcStraightPath(shortestPath);
		s.stop();
		log.addToLog("Time to calculate waypoints: " + s.getElapsedTimeUSecs() + " us");
		
		return waypoints;
	}
	
	public ArrayList<Point> calcStraightPath(ArrayList<Point> shortestPath) {
		if(shortestPath == null || shortestPath.size() <= 2) {
			return shortestPath;
		}
		
		ArrayList<Point> waypoints = new ArrayList<Point>();
		
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
}
