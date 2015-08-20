package pathFinding.tests;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

import pathFinding.AStar;
import pathFinding.AStarMap;
import pathFinding.PathFinder;
import pathFinding.heuristics.AStarHeuristic;
import pathFinding.heuristics.DiagonalHeuristic;
import pathFinding.utils.Logger;
import pathFinding.utils.StopWatch;

public class TestPathFinderGui extends JFrame {
	private static int startX = 50;
	private static int startY = 12;
	private static int goalX = 158;
	private static int goalY = 110;
	private static int cellSize = 8;
	
	private static AStarMapData mapData = new AStarMapData();
	
	private AStarPanel panel = new AStarPanel(mapData.getObstacleMap(), mapData.getMapWidth(), 
			mapData.getMapHeight(), cellSize);
	private JLabel statusBar = new JLabel();
	
	public TestPathFinderGui() {
		add(panel, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);
		
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				
				startX = goalX;
				startY = goalY;
				
				goalX = (e.getX() - panel.getX()) / cellSize;
				goalY = mapData.getMapHeight() - (e.getY() - panel.getY()) / cellSize;
				
				findPath();
			}
		});
	}
	
	public static void main(String[] args) {
		TestPathFinderGui frame = new TestPathFinderGui();
		frame.setTitle("TestAStarGui");
		frame.setSize(mapData.getMapWidth() * cellSize + 100, mapData.getMapHeight() * cellSize + 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		frame.findPath();
	}
	
	private void findPath() {
		StringBuffer sb = new StringBuffer();
		sb.append("start:(").append(startX).append(",").append(startY).append(") ")
		.append("goal:(").append(goalX).append(",").append(goalY).append(")");
		
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
		sb.append(", Time to calculate path:").append(s.getElapsedTime());
		
		log.addToLog("Calculating optimized waypoints...");
		PathFinder pathfinder = new PathFinder(map);
		s.start();
		ArrayList<Point> optimizedPath = pathfinder.calcStraightPath(shortestPath);
		s.stop();
		log.addToLog("Time to calculate waypoints: " + s.getElapsedTime() + " ms");
		sb.append(", Time to calculate waypoints:").append(s.getElapsedTime());
		
		statusBar.setText(sb.toString());
		panel.setPath(shortestPath, optimizedPath);
	}
}
