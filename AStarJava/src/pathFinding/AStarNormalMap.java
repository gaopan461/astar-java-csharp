package pathFinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pathFinding.core.AStarCell;
import pathFinding.core.AStarMap;
import pathFinding.utils.Logger;

public class AStarNormalMap extends AStarMap {
	private int mapWith;
	private int mapHeight;
	private Map<Integer, AStarCell> map = new HashMap<>();

	private Logger log = new Logger();
	
	/**
	 * Class constructor specifying the With, Height and Obstacles of the map.
	 * (no start and goal location)
	 * The Obstacle 2D array map can be any With and Height
	 * @param mapWith		the with of the map as int
	 * @param mapHeight		the Height of the map as int
	 * @param obstacleMap	a 2D int array map of the obstacles on the map. '1' is obstacle, '0' is not.
	 */
	public AStarNormalMap(int mapWith, int mapHeight, int[][] obstacleMap) {
		this.mapWith = mapWith;
		this.mapHeight = mapHeight;
		createMap(obstacleMap);
		log.addToLog("\tMap Created");
	}
	
	/**
	 * Sets up the Cells of the map with the With and Height specified in the constructor
	 * or set methods.
	 */
	private void createMap(int[][] obstacleMap) {
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
	
	public int getMapWith() {
		return mapWith;
	}
	
	public int getMapHeight() {
		return mapHeight;
	}
	
	public ArrayList<AStarCell> getNeighborList(AStarCell cell) {
		ArrayList<AStarCell> neighborList = new ArrayList<AStarCell>();
		if (cell.getY() > 0) {// down
			AStarCell neighbor = getCell(cell.getX(), (cell.getY() - 1));
			if(neighbor != null) {
				neighborList.add(neighbor);
			}
		}
		
		if (cell.getX() < (mapWith - 1) && cell.getY() > 0) {// down right
			AStarCell neighbor = getCell(cell.getX() + 1, cell.getY() - 1);
			if(neighbor != null) {
				neighborList.add(neighbor);
			}
		}
		
		if (cell.getX() < (mapWith - 1)) {// right
			AStarCell neighbor = getCell(cell.getX() + 1, cell.getY());
			if(neighbor != null) {
				neighborList.add(neighbor);
			}
		}
		
		if (cell.getX() < (mapWith - 1) && cell.getY() < (mapHeight - 1)) { // up right
			AStarCell neighbor = getCell(cell.getX() + 1, cell.getY() + 1);
			if(neighbor != null) {
				neighborList.add(neighbor);
			}
		}
		
		if (cell.getY() < (mapHeight - 1)) {// up
			AStarCell neighbor = getCell(cell.getX(), cell.getY() + 1);
			if(neighbor != null) {
				neighborList.add(neighbor);
			}
		}
		
		if (cell.getX() > 0 && cell.getY() < (mapHeight - 1)) {// up left
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
}
