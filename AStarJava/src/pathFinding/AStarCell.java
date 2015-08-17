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
	
	private boolean obstacle;
	private boolean start;
	private boolean goal;
	
	AStarCell(int x, int y, AStarMap map) {
		this.x = x;
		this.y = y;
		this.map = map;
		this.distanceFromStart = Integer.MAX_VALUE;
		this.obstacle = false;
		this.start = false;
		this.goal = false;
	}
	
	AStarCell (int x, int y, AStarMap map, int distanceFromStart, boolean isObstical, boolean isStart, boolean isGoal) {
		this.x = x;
		this.y = y;
		this.map = map;
		this.distanceFromStart = distanceFromStart;
		this.obstacle = isObstical;
		this.start = isStart;
		this.goal = isGoal;
	}
	
	public ArrayList<AStarCell> getNeighborList() {
		ArrayList<AStarCell> neighborList = new ArrayList<AStarCell>();
		if (y > 0) {// down
			neighborList.add(map.getCell(x, (y-1)));
		}
		if (y > 0 && x < (map.getMapWith() - 1)) {// down right
			neighborList.add(map.getCell(x+1,y-1));
		}
		if (x < (map.getMapWith()-1)) {// right
			neighborList.add(map.getCell(x+1,y));
		}
		if (x < (map.getMapWith()-1) && y < (map.getMapHeight()-1)) { // up right
			neighborList.add(map.getCell(x+1,y+1));
		}
		if (y < (map.getMapHeight()-1)) {// up
			neighborList.add(map.getCell(x,y+1));
		}
		if (x > 0 && y < (map.getMapHeight()-1)) {// up left
			neighborList.add(map.getCell(x-1,y+1));
		}
		if (x > 0) {// left
			neighborList.add(map.getCell(x-1,y));
		}
		if (x > 0 && y > 0) {// down left
			neighborList.add(map.getCell(x-1,y-1));
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

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public Point getPoint() {
		return new Point(x,y);
	}
	
	public boolean isObstacle() {
		return obstacle;
	}

	public void setObstacle(boolean obstacle) {
		this.obstacle = obstacle;
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public boolean isGoal() {
		return goal;
	}

	public void setGoal(boolean goal) {
		this.goal = goal;
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