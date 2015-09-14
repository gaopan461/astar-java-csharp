package pathFinding.tests;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import pathFinding.AStarMultiTileMap;
import pathFinding.core.AStar;
import pathFinding.core.AStarCell;
import pathFinding.core.AStarMap;
import pathFinding.core.PathFinder;
import pathFinding.heuristics.AStarHeuristic;
import pathFinding.heuristics.DiagonalHeuristic;
import pathFinding.utils.StopWatch;

public class TestPathFinderGui2 extends JFrame {
	private static final long serialVersionUID = 1L;

	private enum DrawType {
		NONE,
		DRAWING,
		DRAWED,
	}
	
	private static int startX = 50;
	private static int startY = 12;
	private static int goalX = 158;
	private static int goalY = 110;
	private static int cellSize = 12;
	
	private JLabel statusBar = new JLabel();
	
	private static final String[][] TILES = {
		{"project-a-test-dxtsg01-00","project-a-test-dxtsg01-00","project-a-test-dxtsg01-00","project-a-test-dxtsg01-00","project-a-test-dxtsg01-00",},
		{"project-a-test-dxtsg01-00","project-a-test-dxtsg01-00","project-a-test-dxtsg01-00","project-a-test-dxtsg01-00","project-a-test-dxtsg01-00",},
		{"project-a-test-dxtsg01-00","project-a-test-dxtsg01-02","project-a-test-dxtsg01-01","project-a-test-dxtsg01-03","project-a-test-dxtsg01-00",},
		{"project-a-test-dxtsg01-00","project-a-test-dxtsg01-00","project-a-test-dxtsg01-00","project-a-test-dxtsg01-01","project-a-test-dxtsg01-00",},
		{"project-a-test-dxtsg01-00","project-a-test-dxtsg01-00","project-a-test-dxtsg01-02","project-a-test-dxtsg01-03","project-a-test-dxtsg01-00",},
	};
	
	private AStarMap map = new AStarMultiTileMap(TILES, 5, 5);
	private AStarPanel panel = new AStarPanel(map.getWidthInCells(), map.getHeightInCells(), cellSize);
	
	public TestPathFinderGui2() {
		add(panel, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);
	}
	
	public static void main(String[] args) {
		TestPathFinderGui2 frame = new TestPathFinderGui2();
		frame.setTitle("TestAStarGui");
		frame.setSize(frame.panel.mapWidth * cellSize + 100, frame.panel.mapHeight * cellSize + 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		frame.findPath();
	}
	
	private List<Point> findAStarPath() {
		AStarHeuristic heuristic = new DiagonalHeuristic();
		
		AStar aStar = new AStar(map, heuristic);
		
		List<Point> shortestPath = aStar.calcShortestPath(startX, startY, goalX, goalY);
		
		return shortestPath;
	}
	
	private List<Point> findOptimizedPath() {
		PathFinder pathfinder = new PathFinder(map);
		
		List<Point> optimizedPath = pathfinder.findStraightPath(new Point(startX, startY), new Point(goalX, goalY));
		
		return optimizedPath;
	}
	
	private void findPath() {
		StringBuffer sb = new StringBuffer();
		sb.append("start:(").append(startX).append(",").append(startY).append(") ")
		.append("goal:(").append(goalX).append(",").append(goalY).append(")");
		
		StopWatch s = new StopWatch();
		
		s.start();
		List<Point> shortestPath = findAStarPath();
		s.stop();
		
		sb.append(", Time to calculate astar path:").append(s.getElapsedTimeUSecs());
		
		s.start();
		List<Point> optimizedPath = findOptimizedPath();
		s.stop();

		sb.append(", Time to calculate optimized path:").append(s.getElapsedTimeUSecs());
		
		statusBar.setText(sb.toString());
		panel.setPath(shortestPath, optimizedPath);
	}
	
	//-----------------------------------------------------------------------------------------------
	
	class AStarPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private int mapWidth;
		private int mapHeight;
		private int cellSize;
		
		private List<Point> shortestPath = new ArrayList<Point>();
		private int shortestPathDrawIndex = 0;
		private DrawType shortestPathDrawType = DrawType.NONE;
		
		private List<Point> optimizedPath = new ArrayList<Point>();
		private int optimizedPathDrawIndex = 0;
		private DrawType optimizedPathDrawType = DrawType.NONE;
		
		private Timer drawTimer = new Timer(5, new DrawTimerListener());
		
		public AStarPanel(int mapWidth, int mapHeight, int cellSize) {
			super();
			this.mapWidth = mapWidth;
			this.mapHeight = mapHeight;
			this.cellSize = cellSize;
			
			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseClicked(e);
					
					startX = goalX;
					startY = goalY;
					
					goalX = e.getX() / cellSize;
					goalY = e.getY() / cellSize;
					
					TestPathFinderGui2.this.findPath();
				}
			});
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			for(int h = 0; h < mapHeight; ++h) {
				int y = h * cellSize;
				for(int w = 0; w < mapWidth; ++w) {
					int x = w * cellSize;
					AStarCell cell = TestPathFinderGui2.this.map.getCell(w, h);
					if(AStarCell.isObstacle(cell)) {
						g.setColor(Color.BLACK);
						g.fillRect(x, y, cellSize, cellSize);
					} else {
						g.setColor(Color.GRAY);
						g.drawRect(x, y, cellSize, cellSize);
					}
				}
			}
			
			g.setColor(Color.RED);
			switch(shortestPathDrawType) {
			case NONE:
				break;
			case DRAWING:
				shortestPathDrawIndex++;
				drawShortestPath(g, shortestPathDrawIndex);
				if(shortestPathDrawIndex == shortestPath.size()) {
					shortestPathDrawType = DrawType.DRAWED;
				}
				break;
			case DRAWED:
				drawShortestPath(g, shortestPath.size());
				break;
			}
			
			if(shortestPathDrawType == DrawType.DRAWING) {
				return;
			}
			
			g.setColor(Color.BLUE);
			switch(optimizedPathDrawType) {
			case NONE:
				break;
			case DRAWING:
				optimizedPathDrawIndex++;
				drawOptimizedPath(g, optimizedPathDrawIndex);
				if(optimizedPathDrawIndex == optimizedPath.size()) {
					optimizedPathDrawType = DrawType.DRAWED;
				}
				break;
			case DRAWED:
				drawOptimizedPath(g, optimizedPath.size());
				break;
			}
			
			if(shortestPathDrawType != DrawType.DRAWING && optimizedPathDrawType != DrawType.DRAWING) {
				if(drawTimer.isRunning()) {
					drawTimer.stop();
				}
			}
			
		}
		
		private void drawShortestPath(Graphics g, int size) {
			for(int i = 0; i < size; ++i) {
				Point point = shortestPath.get(i);
				int x = point.x * cellSize;
				int y = point.y * cellSize;
				g.fillRect(x, y, cellSize, cellSize);
			}
		}
		
		private void drawOptimizedPath(Graphics g, int size) {
			Polygon polygon = new Polygon();
			for(int i = 0; i < size; ++i) {
				Point point = optimizedPath.get(i);
				int x = point.x * cellSize + cellSize / 2;
				int y = point.y * cellSize + cellSize / 2;
				polygon.addPoint(x, y);
			}
			g.drawPolyline(polygon.xpoints, polygon.ypoints, polygon.npoints);
		}

		public void setPath(List<Point> shortestPath, List<Point> optimizedPath) {
			this.shortestPath = shortestPath;
			this.shortestPathDrawIndex = 0;
			this.optimizedPath = optimizedPath;
			this.optimizedPathDrawIndex = 0;
			
			if(shortestPath != null && shortestPath.size() > 0) {
				this.shortestPathDrawType = DrawType.DRAWING;
			} else {
				this.shortestPathDrawType = DrawType.NONE;
			}
			
			if(optimizedPath != null && optimizedPath.size() > 0) {
				this.optimizedPathDrawType = DrawType.DRAWING;
			} else {
				this.optimizedPathDrawType = DrawType.NONE;
			}
			
			if(this.shortestPathDrawType == DrawType.DRAWING
					|| this.optimizedPathDrawType == DrawType.DRAWING) {
				drawTimer.start();
			} else {
				repaint();
			}
		}
		
		class DrawTimerListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
			
		}
	}
}
