package pathFinding.core;

import java.util.ArrayList;

/**
 * The AreaMap holds information about the With, Height, 
 * Start position, Goal position and Obstacles on the map.
 * A place on the map is referred to by it's (x,y) coordinates, 
 * where (0,0) is the upper left corner, and x is horizontal and y is vertical.
 */
public abstract class AStarMap {
	private static final float SQRT2 = (float)Math.sqrt(2);
	
	protected int widthInCells;
	protected int heightInCells;

	public abstract AStarCell getCell(int x, int y);
	
	public int getWidthInCells() {
		return widthInCells;
	}

	public int getHeightInCells() {
		return heightInCells;
	}

	public ArrayList<AStarCell> getNeighborList(AStarCell cell) {
		ArrayList<AStarCell> neighborList = new ArrayList<AStarCell>();
		if (cell.getY() > 0) {// down
			AStarCell neighbor = getCell(cell.getX(), (cell.getY() - 1));
			if(neighbor != null) {
				neighborList.add(neighbor);
			}
		}
		
		if (cell.getX() < (widthInCells - 1) && cell.getY() > 0) {// down right
			AStarCell neighbor = getCell(cell.getX() + 1, cell.getY() - 1);
			if(neighbor != null) {
				neighborList.add(neighbor);
			}
		}
		
		if (cell.getX() < (widthInCells - 1)) {// right
			AStarCell neighbor = getCell(cell.getX() + 1, cell.getY());
			if(neighbor != null) {
				neighborList.add(neighbor);
			}
		}
		
		if (cell.getX() < (widthInCells - 1) && cell.getY() < (heightInCells - 1)) { // up right
			AStarCell neighbor = getCell(cell.getX() + 1, cell.getY() + 1);
			if(neighbor != null) {
				neighborList.add(neighbor);
			}
		}
		
		if (cell.getY() < (heightInCells - 1)) {// up
			AStarCell neighbor = getCell(cell.getX(), cell.getY() + 1);
			if(neighbor != null) {
				neighborList.add(neighbor);
			}
		}
		
		if (cell.getX() > 0 && cell.getY() < (heightInCells - 1)) {// up left
			AStarCell neighbor = getCell(cell.getX() - 1, cell.getY() + 1);
			if(neighbor != null) {
				neighborList.add(neighbor);
			}
		}
		
		if (cell.getX() > 0) {// left
			AStarCell neighbor = getCell(cell.getX() - 1, cell.getY());
			if(neighbor != null) {
				neighborList.add(neighbor);
			}
		}
		
		if (cell.getX() > 0 && cell.getY() > 0) {// down left
			AStarCell neighbor = getCell(cell.getX() - 1, cell.getY() - 1);
			if(neighbor != null) {
				neighborList.add(neighbor);
			}
		}
		return neighborList;
	}
	
	/**
	 * Determine the distance between two neighbor Cells 
	 * as used by the AStar algorithm.
	 * 
	 * @param cell1 any Cell
	 * @param cell2 any of Cell1's neighbors
	 * @return Float - the distance between the two neighbors
	 */
	public static float getDistanceBetween(AStarCell cell1, AStarCell cell2) {
		//if the cells are on top or next to each other, return 1
		if (cell1.getX() == cell2.getX() || cell1.getY() == cell2.getY()){
			return 1;//*(mapHeight+mapWith);
		} else { //if they are diagonal to each other return diagonal distance: sqrt(1^2+1^2)
			return SQRT2;//*(mapHeight+mapWith);
		}
	}
	
	public static int makeCellId(int x, int y) {
		return y << 16 | x;
	}
	
}
