package pathFinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pathFinding.utils.Logger;

/**
 * The AreaMap holds information about the With, Height, 
 * Start position, Goal position and Obstacles on the map.
 * A place on the map is referred to by it's (x,y) coordinates, 
 * where (0,0) is the upper left corner, and x is horizontal and y is vertical.
 */
public class AStarMap {
	private static final float SQRT2 = (float)Math.sqrt(2);
	
	private int mapWith;
	private int mapHeight;
	private Map<Integer, AStarCell> map = new HashMap<>();
	private int[][] obstacleMap = {{0}};

	private Logger log = new Logger();
	
	/**
	 * Class constructor specifying the With, Height and Obstacles of the map.
	 * (no start and goal location)
	 * The Obstacle 2D array map can be any With and Height
	 * @param mapWith		the with of the map as int
	 * @param mapHeight		the Height of the map as int
	 * @param obstacleMap	a 2D int array map of the obstacles on the map. '1' is obstacle, '0' is not.
	 */
	public AStarMap(int mapWith, int mapHeight, int[][] obstacleMap) {
		this.mapWith = mapWith;
		this.mapHeight = mapHeight;
		this.obstacleMap = obstacleMap;
		createMap();
		log.addToLog("\tMap Created");
	}
	
	/**
	 * Sets up the Cells of the map with the With and Height specified in the constructor
	 * or set methods.
	 */
	private void createMap() {
		map.clear();
		for (int x = 0; x < mapWith; x++) {
			for (int y = 0; y < mapHeight; y++) {
				boolean obstacle = (obstacleMap[y][x] == 1);
				if(obstacle) {
					continue;
				}
				
				AStarCell cell = new AStarCell(this, x, y);
				int cellId = y << 16 | x;
				map.put(cellId, cell);
			}
		}
	}

	public AStarCell getCell(int x, int y) {
		int cellId = y << 16 | x;
		return map.get(cellId);
	}
	
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
	
	public int getMapWith() {
		return mapWith;
	}
	public int getMapHeight() {
		return mapHeight;
	}
	
	/**
	 * Removes all the map information about start location, goal location and obstacles.
	 * Then remakes the map with the original With and Height. 
	 */
	public void clear() {
		createMap();
	}
}
