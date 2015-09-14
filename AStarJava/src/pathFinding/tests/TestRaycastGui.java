package pathFinding.tests;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pathFinding.AStarCellMgr;
import pathFinding.AStarData;
import pathFinding.AStarDataMgr;
import pathFinding.AStarSingleTileMap;
import pathFinding.core.AStarCell;
import pathFinding.core.AStarMap;
import pathFinding.core.PathFinder;
import pathFinding.utils.Logger;
import pathFinding.utils.StopWatch;

public class TestRaycastGui extends JFrame {
	private static final long serialVersionUID = 1L;
	private static int startX = 50;
	private static int startY = 12;
	private static int goalX = 158;
	private static int goalY = 110;
	private static int cellSize = 8;
	
	private static final String TILE_ID = "normal1";
	
	private AStarCellMgr cellMgr = new AStarCellMgr();
	private static AStarData astarData = AStarDataMgr.getAstarData(TILE_ID);
	
	private AStarMap map = new AStarSingleTileMap(TILE_ID, cellMgr);
	
	private AStarPanel panel = new AStarPanel(astarData.getObstacleInfo(), astarData.getWidthInCells(), 
			astarData.getHeightInCells(), cellSize);
	
	public TestRaycastGui() {
		add(panel);
	}
	
	public static void main(String[] args) {
		TestRaycastGui frame = new TestRaycastGui();
		frame.setTitle("TestAStarGui");
		frame.setSize(astarData.getWidthInCells() * cellSize + 100, astarData.getHeightInCells() * cellSize + 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		frame.raycast();
	}
	
	private void raycast() {
		Logger log = new Logger();
		StopWatch s = new StopWatch();
		
		PathFinder pathfinder = new PathFinder(map);
		s.start();
		Point start = new Point(startX, startY);
		Point goal = new Point(goalX, goalY);
		Point hitPoint = pathfinder.raycast(start, goal);
		s.stop();
		log.addToLog("Time to raycast: " + s.getElapsedTimeUSecs() + " us");
		
		List<Point> path = new ArrayList<Point>();
		path.add(start);
		path.add(hitPoint);
		
		panel.setPath(path);
	}
	
	//-----------------------------------------------------------------------------------------
	
	class AStarPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private int mapWidth;
		private int mapHeight;
		private int cellSize;
		
		private List<Point> optimizedPath = new ArrayList<Point>();
		
		public AStarPanel(int[][] map, int mapWidth, int mapHeight, int cellSize) {
			super();
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
					AStarCell cell = TestRaycastGui.this.map.getCell(w, h);
					if(AStarCell.isObstacle(cell)) {
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

		public void setPath(List<Point> optimizedPath) {
			this.optimizedPath = optimizedPath;
			repaint();
		}
	}
}
