package org.gaopan.astar;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class AStarMapEditor extends JFrame {
	public enum  OperatorType{
		fill,
		clear,
	}
	
	private static final int DEFAULT_CELL_WIDTH = 40;
	private static final int DEFAULT_CELL_HEIGHT = 40;
	private static final int DEFAULT_CELL_SIZE = 1;
	
	private AStarMapEditorPanel editorPanel = new AStarMapEditorPanel(
			DEFAULT_CELL_WIDTH, DEFAULT_CELL_HEIGHT, DEFAULT_CELL_SIZE);
	private AStarMapToolbarPanel toolbarPanel = new AStarMapToolbarPanel(this);
	private JLabel statusBar = new JLabel("Status bar");
	
	public AStarMapEditor() {
		add(editorPanel, BorderLayout.CENTER);
		add(toolbarPanel, BorderLayout.NORTH);
		add(statusBar, BorderLayout.SOUTH);
	}

	public static void main(String[] args) {
		AStarMapEditor frame = new AStarMapEditor();
		frame.setTitle("TestAStarGui");
		frame.setSize();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	private void setSize() {
		this.setSize(editorPanel.getPreferredWidth() + 50, editorPanel.getPreferredHeight() + 150);
	}

	public AStarMapEditorPanel getEditorPanel() {
		return editorPanel;
	}

	public JLabel getStatusBar() {
		return statusBar;
	}
	
	public void setCellWidth(int cellWidth) {
		editorPanel.setCellWidth(cellWidth);
		setSize();
	}
	
	public void setCellHeight(int cellHeight) {
		editorPanel.setCellHeight(cellHeight);
		setSize();
	}
	
	public void setOperator(OperatorType type) {
		editorPanel.setOperator(type);
		String typeName = type.equals(OperatorType.fill) ? "fill" : "clear";
		statusBar.setText("Operator type:" + typeName);
	}
	
	public void loadMap(int[][] newMap, int newWidth, int newHeight) {
		editorPanel.loadMap(newMap, newWidth, newHeight);
		toolbarPanel.setCellWidth(newWidth);
		toolbarPanel.setCellHeight(newHeight);
		setSize();
	}

}
