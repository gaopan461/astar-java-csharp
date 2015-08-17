package pathFinding;

import java.awt.Point;
import java.util.ArrayList;

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
	private ArrayList<ArrayList<AStarCell>> map;
	private int startLocationX = 0;
	private int startLocationY = 0;
	private int goalLocationX = 0;
	private int goalLocationY = 0;
	private int[][] obstacleMap = {{0}};

	private Logger log = new Logger();
	
	/**
	 * Class constructor specifying the With and Height of a otherwise empty map.
	 * (no start and goal location or obstacles)
	 * @param mapWith
	 * @param mapHeight
	 */
	public AStarMap(int mapWith, int mapHeight) {
		this.mapWith = mapWith;
		this.mapHeight = mapHeight;
		
		createMap();
		log.addToLog("\tMap Created");
	}
	
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
		AStarCell cell;
		map = new ArrayList<ArrayList<AStarCell>>();
		for (int x = 0; x < mapWith; x++) {
			map.add(new ArrayList<AStarCell>());
			for (int y = 0; y < mapHeight; y++) {
				cell = new AStarCell(x,y,this);
				try {
					if (obstacleMap[y][x] == 1)
						cell.setObstacle(true);
				} catch (Exception e) {}
				map.get(x).add(cell);
			}
		}
	}

	public void setObstacle(int x, int y, boolean isObstical) {
		map.get(x).get(y).setObstacle(isObstical);
	}

	public AStarCell getCell(int x, int y) {
		return map.get(x).get(y);
	}

	public void setStartLocation(int x, int y) {
		map.get(startLocationX).get(startLocationY).setStart(false);
		map.get(x).get(y).setStart(true);
		startLocationX = x;
		startLocationY = y;
	}

	public void setGoalLocation(int x, int y) {
		map.get(goalLocationX).get(goalLocationY).setGoal(false);
		map.get(x).get(y).setGoal(true);
		goalLocationX = x;
		goalLocationY = y;
	}

	public int getStartLocationX() {
		return startLocationX;
	}

	public int getStartLocationY() {
		return startLocationY;
	}
	
	public AStarCell getStartCell() {
		return map.get(startLocationX).get(startLocationY);
	}

	public int getGoalLocationX() {
		return goalLocationX;
	}

	public int getGoalLocationY() {
		return goalLocationY;
	}
	
	public Point getGoalPoint() {
		return new Point(goalLocationX, goalLocationY);
	}
	
	/**
	 * @return Cell	The Goal Cell
	 * @see AStarCell
	 */
	public AStarCell getGoalCell() {
		return map.get(goalLocationX).get(goalLocationY);
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
		startLocationX = 0;
		startLocationY = 0;
		goalLocationX = 0;
		goalLocationY = 0;
		createMap();
	}
}
