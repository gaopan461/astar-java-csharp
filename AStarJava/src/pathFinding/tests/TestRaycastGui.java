package pathFinding.tests;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pathFinding.AStarNormalMap;
import pathFinding.core.AStarMap;
import pathFinding.core.PathFinder;
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
		AStarMap map = new AStarNormalMap(mapData.getMapWidth(), mapData.getMapHeight(), mapData.getObstacleMap());
		
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
		
		panel.setPath(path);
	}
	
	//-----------------------------------------------------------------------------------------
	
	class AStarPanel extends JPanel {
		private int mapWidth;
		private int mapHeight;
		private int[][] map;
		private int cellSize;
		
		private ArrayList<Point> optimizedPath = new ArrayList<Point>();
		
		public AStarPanel(int[][] map, int mapWidth, int mapHeight, int cellSize) {
			super();
			this.map = map;
			this.mapWidth = mapWidth;
			this.mapHeight = mapHeight;
			this.cellSize = cellSize;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			for(int h = 0; h < mapHeight; ++h) {
				int y = (mapHeight - h) * cellSize;
				for(int w = 0; w < mapWidth; ++w) {
					int x = w * cellSize;
					boolean isObstacle = (map[h][w] == 1 ? true : false);
					if(isObstacle) {
						g.setColor(Color.BLACK);
						g.fillRect(x, y, cellSize, cellSize);
					} else {
						g.setColor(Color.GRAY);
						g.drawRect(x, y, cellSize, cellSize);
					}
				}
			}
			
			g.setColor(Color.BLUE);
			drawOptimizedPath(g, optimizedPath.size());
		}
		
		private void drawOptimizedPath(Graphics g, int size) {
			Polygon polygon = new Polygon();
			for(int i = 0; i < size; ++i) {
				Point point = optimizedPath.get(i);
				int x = point.x * cellSize + cellSize / 2;
				int y = (mapHeight - point.y) * cellSize + cellSize / 2;
				polygon.addPoint(x, y);
			}
			g.drawPolyline(polygon.xpoints, polygon.ypoints, polygon.npoints);
		}

		public void setPath(ArrayList<Point> optimizedPath) {
			this.optimizedPath = optimizedPath;
			repaint();
		}
	}
}
