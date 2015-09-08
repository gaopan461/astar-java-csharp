package org.gaopan.astar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

public class AStarMapEditorPanel extends JPanel {
	private int cellWidth;
	private int cellHeight;
	private int[][] map;
	private int cellSize;
	
	private AStarMapEditor.OperatorType operator = AStarMapEditor.OperatorType.fill;
	
	public AStarMapEditorPanel(int cellWidth, int cellHeight, int cellSize) {
		super();
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		this.map = new int[cellHeight][cellWidth];
		this.cellSize = cellSize;
		
		this.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				super.mouseDragged(e);
				int cellX = e.getX() / AStarMapEditorPanel.this.cellSize;
				int cellY = AStarMapEditorPanel.this.cellHeight - e.getY() / AStarMapEditorPanel.this.cellSize;
				
				if(cellX < 0 || cellX >= AStarMapEditorPanel.this.cellWidth 
						|| cellY < 0 || cellY >= AStarMapEditorPanel.this.cellHeight) {
					return;
				}
				
				if(AStarMapEditorPanel.this.operator == AStarMapEditor.OperatorType.fill) {
					map[cellY][cellX] = AStarMapEditor.OBSTACLE_VALUE;
				} else {
					map[cellY][cellX] = AStarMapEditor.PASS_VALUE;
				}
				
				repaint();
			}
		});
		
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if(e.getButton() == MouseEvent.BUTTON1) {
					AStarMapEditorPanel.this.operator = AStarMapEditor.OperatorType.fill;
				} else {
					AStarMapEditorPanel.this.operator = AStarMapEditor.OperatorType.clear;
				}
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for(int h = 0; h < cellHeight; ++h) {
			int y = h * cellSize;
			for(int w = 0; w < cellWidth; ++w) {
				int x = w * cellSize;
				boolean isObstacle = (map[h][w] == AStarMapEditor.OBSTACLE_VALUE ? true : false);
				if(isObstacle) {
					g.setColor(Color.BLACK);
					g.fillRect(x, y, cellSize, cellSize);
				} else {
					g.setColor(Color.GRAY);
					g.drawRect(x, y, cellSize, cellSize);
				}
			}
		}
	}

	public int getPreferredWidth() {
		return cellWidth * cellSize;
	}

	public int getPreferredHeight() {
		return cellHeight * cellSize;
	}

	public int getCellWidth() {
		return cellWidth;
	}
	
	public int[][] getMap() {
		return map;
	}

	public void setCellWidth(int cellWidth) {
		if(cellWidth <= 0) {
			throw new RuntimeException("Invalid cell width:" + cellWidth);
		}
		
		this.rebuildMap(cellWidth, this.cellHeight);
	}

	public int getCellHeight() {
		return cellHeight;
	}

	public void setCellHeight(int cellHeight) {
		if(cellHeight <= 0) {
			throw new RuntimeException("Invalid cell height:" + cellHeight);
		}
		
		this.rebuildMap(this.cellWidth, cellHeight);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getPreferredWidth(), getPreferredHeight());
	}
	
	private void rebuildMap(int newWidth, int newHeight) {
		int[][] newMap = new int[newHeight][newWidth];
		for(int h = 0; h < newHeight; ++h) {
			for(int w = 0; w < newWidth; ++w) {
				if(h < this.cellHeight && w < this.cellWidth) {
					newMap[h][w] = map[h][w];
				} else {
					newMap[h][w] = AStarMapEditor.OBSTACLE_VALUE;
				}
			}
		}
		
		this.cellWidth = newWidth;
		this.cellHeight = newHeight;
		this.map = newMap;
		this.repaint();
	}

	public void setOperator(AStarMapEditor.OperatorType operator) {
		this.operator = operator;
	}
	
	public void fillAll() {
		for(int h = 0; h < cellHeight; ++h) {
			for(int w = 0; w < cellWidth; ++w) {
				map[h][w] = AStarMapEditor.OBSTACLE_VALUE;
			}
		}
		
		repaint();
	}
	
	public void clearAll() {
		for(int h = 0; h < cellHeight; ++h) {
			for(int w = 0; w < cellWidth; ++w) {
				map[h][w] = AStarMapEditor.PASS_VALUE;
			}
		}
		
		repaint();
	}
	
	public void loadMap(int[][] newMap, int newWidth, int newHeight) {
		this.cellWidth = newWidth;
		this.cellHeight = newHeight;
		this.map = newMap;
		this.repaint();
	}

}
