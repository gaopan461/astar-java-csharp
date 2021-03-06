package pathFinding.tests;

import java.awt.Point;
import java.util.ArrayList;

import pathFinding.AStar;
import pathFinding.AStarMap;
import pathFinding.graphics.PrintMap;
import pathFinding.heuristics.AStarHeuristic;
import pathFinding.heuristics.DiagonalHeuristic;
import pathFinding.utils.Logger;
import pathFinding.utils.StopWatch;

public class TestAStar {
	private static int startX = 50;
	private static int startY = 12;
	private static int goalX = 110;
	private static int goalY = 75;
	
	private static AStarMapData mapData = new AStarMapData();
	
	public static void main(String[] args) {
		Logger log = new Logger();
		StopWatch s = new StopWatch();
		
		log.addToLog("Map initializing...");
		AStarMap map = new AStarMap(mapData.getMapWidth(), mapData.getMapHeight(), mapData.getObstacleMap());
		
		log.addToLog("Heuristic initializing...");
		//AStarHeuristic heuristic = new ClosestHeuristic();
		AStarHeuristic heuristic = new DiagonalHeuristic();
		
		log.addToLog("AStar initializing...");
		AStar aStar = new AStar(map, heuristic);
		
		log.addToLog("Calculating shortest path...");
		s.start();
		ArrayList<Point> shortestPath = aStar.calcShortestPath(startX, startY, goalX, goalY);
		s.stop();
		
		log.addToLog("Time to calculate path in milliseconds: " + s.getElapsedTime());
		
		log.addToLog("Printing map of shortest path...");
		new PrintMap(map, shortestPath);
	}

}
