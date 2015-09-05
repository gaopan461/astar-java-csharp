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
		
		int dx = Math.abs(goal.x - start.x);
		int dy = Math.abs(goal.y - start.y);
		
		int sx = start.x < goal.x ? 1 : -1; 
		int sy = start.y < goal.y ? 1 : -1; 
		
		int err = dx-dy;
		int e2;
		int currentX = start.x;
		int currentY = start.y;
		
		Point hitPoint = new Point(currentX, currentY);
		while(true) {
			AStarCell cell = map.getCell(currentX, currentY); 
			if(AStarCell.isObstacle(cell)) {
				break;
			} else {
				hitPoint = new Point(currentX, currentY);
			}
			
			if(currentX == goal.x && currentY == goal.y) {
				break;
			}
			
			e2 = 2*err;
			if(e2 > -1 * dy) {
				err = err - dy;
				currentX = currentX + sx;
			}
			
			if(e2 < dx) {
				err = err + dx;
				currentY = currentY + sy;
			}
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
		log.addToLog("Time to calculate waypoints: " + s.getElapsedTime() + " ms");
		
		return waypoints;
	}
	
	public ArrayList<Point> calcStraightPath(ArrayList<Point> shortestPath) {
		if(shortestPath == null || shortestPath.isEmpty()) {
			return null;
		}
		
		ArrayList<Point> waypoints = new ArrayList<Point>();
		
		Point p1 = shortestPath.get(0);
		int p1Number = 0;
		waypoints.add(p1);
		
		Point p2 = shortestPath.get(1);
		int p2Number = 1;
		
		while(!p2.equals(shortestPath.get(shortestPath.size()-1))) {
			if(lineClear(p1, p2)) {
				//make p2 the next point in the path
				p2Number++;
				p2 = shortestPath.get(p2Number);
			} else {
				p1Number = p2Number-1;
				p1 = shortestPath.get(p1Number);
				waypoints.add(p1);
				log.addToLog("Got waypoint: " + p1.toString());
				p2Number++;
				p2 = shortestPath.get(p2Number);
			}
		}
		waypoints.add(p2);
		
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
