package pathFinding.tests;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

class AStarPanel extends JPanel {
	private enum DrawType {
		NONE,	// 不需要画
		DRAWING,// 正在画
		DRAWED,	// 已画完
	}
	
	private int mapWidth;
	private int mapHeight;
	private int[][] map;
	private int cellSize;
	
	private ArrayList<Point> shortestPath = new ArrayList<Point>();
	private int shortestPathDrawIndex = 0;
	private DrawType shortestPathDrawType = DrawType.NONE;
	
	private ArrayList<Point> optimizedPath = new ArrayList<Point>();
	private int optimizedPathDrawIndex = 0;
	private DrawType optimizedPathDrawType = DrawType.NONE;
	
	private Timer drawTimer = new Timer(50, new DrawTimerListener());
	
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
			int y = (mapHeight - point.y) * cellSize;
			g.fillRect(x, y, cellSize, cellSize);
		}
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

	public void setPath(ArrayList<Point> shortestPath, ArrayList<Point> optimizedPath) {
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