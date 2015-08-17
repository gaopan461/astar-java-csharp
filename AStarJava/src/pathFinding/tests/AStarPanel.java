package pathFinding.tests;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import javax.swing.JPanel;

class AStarPanel extends JPanel {
	private int mapWidth;
	private int mapHeight;
	private int[][] map;
	private int cellSize;
	
	private ArrayList<Point> shortestPath = new ArrayList<Point>();
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
		
		g.setColor(Color.RED);
		for(Point point : shortestPath) {
			int x = point.x * cellSize;
			int y = (mapHeight - point.y) * cellSize;
			g.fillRect(x, y, cellSize, cellSize);
		}
		
		g.setColor(Color.BLUE);
		Polygon polygon = new Polygon();
		for(Point point : optimizedPath) {
			int x = point.x * cellSize + cellSize / 2;
			int y = (mapHeight - point.y) * cellSize + cellSize / 2;
			polygon.addPoint(x, y);
		}
		g.drawPolyline(polygon.xpoints, polygon.ypoints, polygon.npoints);
	}

	public void setShortestPath(ArrayList<Point> shortestPath) {
		this.shortestPath = shortestPath;
		this.repaint();
	}

	public void setOptimizedPath(ArrayList<Point> optimizedPath) {
		this.optimizedPath = optimizedPath;
		this.repaint();
	}
	
}