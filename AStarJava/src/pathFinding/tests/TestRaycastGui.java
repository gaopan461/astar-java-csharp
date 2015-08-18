package pathFinding.tests;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JFrame;

import pathFinding.AStarMap;
import pathFinding.PathFinder;
import pathFinding.utils.Logger;
import pathFinding.utils.StopWatch;

public class TestRaycastGui extends JFrame {
	private static int startX = 50;
	private static int startY = 12;
	private static int goalX = 158;
	private static int goalY = 110;
	private static int cellSize = 8;
	
	private static AStarMapData mapData = new AStarMapData();
	
	private AStarPanel panel = new AStarPanel(mapData.getObstacleMap(), mapData.getMapWidth(), 
			mapData.getMapHeight(), cellSize);
	
	public TestRaycastGui() {
		add(panel);
	}
	
	public static void main(String[] args) {
		TestRaycastGui frame = new TestRaycastGui();
		frame.setTitle("TestAStarGui");
		frame.setSize(mapData.getMapWidth() * cellSize + 100, mapData.getMapHeight() * cellSize + 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		frame.raycast();
	}
	
	private void raycast() {
		Logger log = new Logger();
		StopWatch s = new StopWatch();
		
		log.addToLog("Map initializing...");
		AStarMap map = new AStarMap(mapData.getMapWidth(), mapData.getMapHeight(), mapData.getObstacleMap());
		
		PathFinder pathfinder = new PathFinder(map);
		s.start();
		Point start = new Point(startX, startY);
		Point goal = new Point(goalX, goalY);
		Point hitPoint = pathfinder.raycast(start, goal);
		s.stop();
		log.addToLog("Time to raycast: " + s.getElapsedTime() + " ms");
		
		ArrayList<Point> path = new ArrayList<Point>();
		path.add(start);
		path.add(hitPoint);
		
		panel.setOptimizedPath(path);
	}
}
