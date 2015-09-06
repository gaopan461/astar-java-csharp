package pathFinding.tests.randomMap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import pathFinding.core.AStar;
import pathFinding.core.AStarMap;
import pathFinding.core.PathFinder;
import pathFinding.heuristics.AStarHeuristic;
import pathFinding.heuristics.DiagonalHeuristic;
import pathFinding.utils.Logger;
import pathFinding.utils.StopWatch;

public class MapPanel extends JPanel {
	private TestRandomMap frame;
	private float cellSize;
	
	private Image[] images;
	private int widthCount;
	private int heightCount;
	
	private AStarMap astarMap;
	private Point start;
	private Point goal;
	
	private boolean drawing = false;
	
	private ArrayList<Point> shortestPathDrawed = new ArrayList<>();
	private ArrayList<Point> optimizedPathDrawed = new ArrayList<>();
	private ArrayList<Point> shortestPathUndraw = new ArrayList<>();
	private ArrayList<Point> optimizedPathUndraw = new ArrayList<>();
	
	private Timer shortestPathDrawTimer = new Timer(50, new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(shortestPathUndraw == null || shortestPathUndraw.isEmpty()) {
				shortestPathDrawTimer.stop();
				optimizedPathDrawTimer.start();
			} else {
				int num = Math.min(5, shortestPathUndraw.size());
				for(int i = 0; i < num; ++i) {
					shortestPathDrawed.add(shortestPathUndraw.remove(0));
				}
				MapPanel.this.repaint();
			}
		}
	});
	
	private Timer optimizedPathDrawTimer = new Timer(100, new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(optimizedPathUndraw == null || optimizedPathUndraw.isEmpty()) {
				optimizedPathDrawTimer.stop();
				MapPanel.this.drawing = false;
			} else {
				optimizedPathDrawed.add(optimizedPathUndraw.remove(0));
				MapPanel.this.repaint();
			}
		}
	});
	
	private Logger log = new Logger();
	
	public Point pixelPos2CellPos(int x, int y) {
		int cellx = (int)(x / cellSize);
		int celly = (int)(y / cellSize);
		return new Point(cellx, celly);
	}
	
	public MapPanel(TestRandomMap frame, float cellSize) {
		super();
		this.frame = frame;
		this.cellSize = cellSize;
		
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(MapPanel.this.drawing) {
					return;
				}
				
				log.addToLog("mouseClicked:" + e.getX() + "," + e.getY());
				
				super.mouseClicked(e);
				MapPanel.this.start = MapPanel.this.goal;
				MapPanel.this.goal = pixelPos2CellPos(e.getX(), e.getY());
				
				if(MapPanel.this.start != null && MapPanel.this.goal != null) {
					MapPanel.this.findPath();
				}
			}
		});
	}
	
	public void setImages(Image[] images, int widthCount, int heightCount) {
		this.images = images;
		this.widthCount = widthCount;
		this.heightCount = heightCount;
		
		if(images.length != widthCount * heightCount) {
			throw new RuntimeException("invalid args");
		}
		
		repaint();
	}
	
	public void setAstar(AStarMap astarMap) {
		this.astarMap = astarMap;
		
		start = null;
		goal = null;
		this.shortestPathDrawed.clear();
		this.optimizedPathDrawed.clear();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int cellWidthSize = getWidth() / widthCount;
		int cellHeightSize = getHeight() / heightCount;
		
		for(int h = 0; h < heightCount; ++h) {
			for(int w = 0; w < widthCount; ++w) {
				Image image = images[h * widthCount + w];
				int startx = w * cellWidthSize;
				int starty = h * cellHeightSize;
				g.drawImage(image, startx, starty, cellWidthSize, cellHeightSize, this);
			}
		}
		
		drawShortestPath(g);
		drawOptimizedPath(g);
	}
	
	private void drawShortestPath(Graphics g) {
		g.setColor(Color.RED);
		for(Point point : shortestPathDrawed) {
			int x = (int)(point.x * cellSize);
			int y = (int)(point.y * cellSize);
			g.fillRect(x, y, (int)cellSize, (int)cellSize);
		}
	}
	
	private void drawOptimizedPath(Graphics g) {
		g.setColor(Color.GREEN);
		Polygon polygon = new Polygon();
		for(Point point : optimizedPathDrawed) {
			int x = (int)(point.x * cellSize + cellSize / 2);
			int y = (int)(point.y * cellSize + cellSize / 2);
			polygon.addPoint(x, y);
		}
		g.drawPolyline(polygon.xpoints, polygon.ypoints, polygon.npoints);
	}
	
	private ArrayList<Point> findAStarPath() {
		AStarHeuristic heuristic = new DiagonalHeuristic();
		
		AStar aStar = new AStar(astarMap, heuristic);
		
		ArrayList<Point> shortestPath = aStar.calcShortestPath(start.x, start.y, goal.x, goal.y);
		
		return shortestPath;
	}
	
	private ArrayList<Point> findOptimizedPath() {
		PathFinder pathfinder = new PathFinder(astarMap);
		
		ArrayList<Point> optimizedPath = pathfinder.findStraightPath(start, goal);
		
		return optimizedPath;
	}
	
	private void findPath() {
		StringBuffer sb = new StringBuffer();
		sb.append("start:(").append(start.x).append(",").append(start.y).append(") ")
		.append("goal:(").append(goal.x).append(",").append(goal.y).append(")");
		
		StopWatch s = new StopWatch();
		
		s.start();
		ArrayList<Point> shortestPath = findAStarPath();
		s.stop();
		
		sb.append(", Time to calculate astar path:").append(s.getElapsedTimeUSecs());
		
		s.start();
		ArrayList<Point> optimizedPath = findOptimizedPath();
		s.stop();

		sb.append(", Time to calculate optimized path:").append(s.getElapsedTimeUSecs());
		
		frame.getStatusBar().setText(sb.toString());
		
		drawPath(shortestPath, optimizedPath);
	}
	
	private void drawPath(ArrayList<Point> shortestPath, ArrayList<Point> optimizedPath) {
		this.shortestPathUndraw = shortestPath;
		this.optimizedPathUndraw = optimizedPath;
		
		this.shortestPathDrawed.clear();
		this.optimizedPathDrawed.clear();
		
		if(shortestPath != null && !shortestPath.isEmpty() && optimizedPath != null && !optimizedPath.isEmpty()) {
			shortestPathDrawTimer.start();
			this.drawing = true;
		} else {
			this.repaint();
		}
	}
	
}
