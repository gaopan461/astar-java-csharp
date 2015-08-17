package pathFinding;

import java.awt.Point;
import java.util.ArrayList;

import pathFinding.bresenhamsLine.BresenhamsLine;
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
	
	public ArrayList<Point> getWaypoints() {
		log.addToLog("AStar Heuristic initializing...");
		AStarHeuristic heuristic = new DiagonalHeuristic();
		
		log.addToLog("AStar initializing...");
		AStar aStar = new AStar(map, heuristic);
		
		log.addToLog("Calculating shortest path with AStar...");
		ArrayList<Point> shortestPath = aStar.calcShortestPath(map.getStartLocationX(), map.getStartLocationY(), map.getGoalLocationX(), map.getGoalLocationY());
		
		//log.addToLog("Printing map of shortest path...");
		//new PrintMap(map, shortestPath);
		
		log.addToLog("Calculating optimized waypoints...");
		s.start();
		ArrayList<Point> waypoints = calculateWayPoints(shortestPath);
		s.stop();
		log.addToLog("Time to calculate waypoints: " + s.getElapsedTime() + " ms");
		
		return waypoints;
	}
	
	public ArrayList<Point> calculateWayPoints(ArrayList<Point> shortestPath) {
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
		ArrayList<Point> pointsOnLine = BresenhamsLine.getCellsOnLine(a, b);
		for(Point p : pointsOnLine) {
			if(map.getCell(p.x, p.y).isObstacle()) {
				return false;
			}
		}
		return true;
	}
}
