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
	
	public abstract AStarCell getCell(int x, int y);
	
	public abstract ArrayList<AStarCell> getNeighborList(AStarCell cell);
	
	public abstract int getMapWith();
	
	public abstract int getMapHeight();
	
	/**
	 * Determine the distance between two neighbor Cells 
	 * as used by the AStar algorithm.
	 * 
	 * @param cell1 any Cell
	 * @param cell2 any of Cell1's neighbors
	 * @return Float - the distance between the two neighbors
	 */
	public float getDistanceBetween(AStarCell cell1, AStarCell cell2) {
		//if the cells are on top or next to each other, return 1
		if (cell1.getX() == cell2.getX() || cell1.getY() == cell2.getY()){
			return 1;//*(mapHeight+mapWith);
		} else { //if they are diagonal to each other return diagonal distance: sqrt(1^2+1^2)
			return SQRT2;//*(mapHeight+mapWith);
		}
	}
	
}
