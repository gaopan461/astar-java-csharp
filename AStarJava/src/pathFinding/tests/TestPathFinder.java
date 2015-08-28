package pathFinding.tests;

import java.awt.Point;
import java.util.ArrayList;

import pathFinding.AStarNormalMap;
import pathFinding.core.AStarMap;
import pathFinding.core.PathFinder;
import pathFinding.graphics.PrintMap;
import pathFinding.utils.Logger;
import pathFinding.utils.StopWatch;

public class TestPathFinder {
	private static int startX = 50;
	private static int startY = 12;
	private static int goalX = 158;
	private static int goalY = 110;
	
	private static AStarMapData mapData = new AStarMapData();
	
	public static void main(String[] args) {
		Logger log = new Logger();
		StopWatch s = new StopWatch();
		
		log.addToLog("Map initializing...");
		AStarMap map = new AStarNormalMap(mapData.getMapWidth(), mapData.getMapHeight(), mapData.getObstacleMap());
		
		s.start();
		
		PathFinder pathfinder = new PathFinder(map);
		Point start = new Point(startX, startY);
		Point goal = new Point(goalX, goalY);
		ArrayList<Point> optimizedWaypoints = pathfinder.findStraightPath(start, goal);
		
		s.stop();
		log.addToLog("Total pathfinding took: " + s.getElapsedTime() + " ms");
		
		log.addToLog("Printing map of optimized path...");
		new PrintMap(map, optimizedWaypoints);
	}
}
