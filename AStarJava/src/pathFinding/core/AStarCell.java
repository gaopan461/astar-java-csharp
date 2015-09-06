package pathFinding.core;

import java.awt.Point;

public class AStarCell implements Comparable<AStarCell> {
	/* Cells that this is connected to */
	private float distanceFromStart;
	private float heuristicDistanceFromGoal;
	private AStarCell previousCell;
	private int x;
	private int y;
	
	public AStarCell(int x, int y) {
		this.x = x;
		this.y = y;
		this.distanceFromStart = Integer.MAX_VALUE;
	}

	public float getDistanceFromStart() {
		return distanceFromStart;
	}

	public void setDistanceFromStart(float f) {
		this.distanceFromStart = f;
	}

	public AStarCell getPreviousCell() {
		return previousCell;
	}

	public void setPreviousCell(AStarCell previousCell) {
		this.previousCell = previousCell;
	}
	
	public float getHeuristicDistanceFromGoal() {
		return heuristicDistanceFromGoal;
	}

	public void setHeuristicDistanceFromGoal(float heuristicDistanceFromGoal) {
		this.heuristicDistanceFromGoal = heuristicDistanceFromGoal;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public Point getPoint() {
		return new Point(x,y);
	}
	
	public boolean isObstacle() {
		return false;
	}

	@Override
	public boolean equals(Object arg0) {
		AStarCell cell = (AStarCell)arg0;
		return (cell.x == x) && (cell.y == y);
	}

	@Override
	public int compareTo(AStarCell otherCell) {
		float thisTotalDistanceFromGoal = heuristicDistanceFromGoal + distanceFromStart;
		float otherTotalDistanceFromGoal = otherCell.getHeuristicDistanceFromGoal() + otherCell.getDistanceFromStart();
		
		if (Math.abs(thisTotalDistanceFromGoal - otherTotalDistanceFromGoal) < 0.01 ) {
			return 0;
		} else if (thisTotalDistanceFromGoal < otherTotalDistanceFromGoal) {
			return -1;
		} else {
			return 1;
		}
	}
	
	public void reset() {
		this.distanceFromStart = Integer.MAX_VALUE;
		this.heuristicDistanceFromGoal = 0;
		this.previousCell = null;
	}
	
	public static boolean isObstacle(AStarCell cell) {
		return (cell == null) || cell.isObstacle();
	}
}