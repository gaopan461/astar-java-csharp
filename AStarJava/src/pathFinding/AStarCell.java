package pathFinding;

import java.awt.Point;
import java.util.ArrayList;

public class AStarCell implements Comparable<AStarCell> {
	/* Cells that this is connected to */
	private AStarMap map;
	private float distanceFromStart;
	private float heuristicDistanceFromGoal;
	private AStarCell previousCell;
	private int x;
	private int y;
	
	AStarCell(AStarMap map, int x, int y) {
		this.map = map;
		this.x = x;
		this.y = y;
		this.distanceFromStart = Integer.MAX_VALUE;
	}
	
	public ArrayList<AStarCell> getNeighborList() {
		ArrayList<AStarCell> neighborList = new ArrayList<AStarCell>();
		if (y > 0) {// down
			AStarCell cell = map.getCell(x, (y-1));
			if(cell != null) {
				neighborList.add(cell);
			}
		}
		
		if (y > 0 && x < (map.getMapWith() - 1)) {// down right
			AStarCell cell = map.getCell(x+1,y-1);
			if(cell != null) {
				neighborList.add(cell);
			}
		}
		
		if (x < (map.getMapWith()-1)) {// right
			AStarCell cell = map.getCell(x+1,y);
			if(cell != null) {
				neighborList.add(cell);
			}
		}
		
		if (x < (map.getMapWith()-1) && y < (map.getMapHeight()-1)) { // up right
			AStarCell cell = map.getCell(x+1,y+1);
			if(cell != null) {
				neighborList.add(cell);
			}
		}
		
		if (y < (map.getMapHeight()-1)) {// up
			AStarCell cell = map.getCell(x,y+1);
			if(cell != null) {
				neighborList.add(cell);
			}
		}
		
		if (x > 0 && y < (map.getMapHeight()-1)) {// up left
			AStarCell cell = map.getCell(x-1,y+1);
			if(cell != null) {
				neighborList.add(cell);
			}
		}
		
		if (x > 0) {// left
			AStarCell cell = map.getCell(x-1,y);
			if(cell != null) {
				neighborList.add(cell);
			}
		}
		
		if (x > 0 && y > 0) {// down left
			AStarCell cell = map.getCell(x-1,y-1);
			if(cell != null) {
				neighborList.add(cell);
			}
		}
		return neighborList;
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

	public boolean equals(AStarCell cell) {
		return (cell.x == x) && (cell.y == y);
	}

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
}