package pathFinding;

import java.util.ArrayList;

import pathFinding.core.AStarCell;
import pathFinding.core.AStarMap;

public class AStarNormalMap extends AStarMap {
	private String tileId;
	private AStarCellMgr cellMgr;
	
	private int mapWidth;
	private int mapHeight;
	
	public AStarNormalMap(String tileId, AStarCellMgr cellMgr) {
		this.tileId = tileId;
		this.cellMgr = cellMgr;
		
		AStarData astarData = AStarDataMgr.getAstarData(tileId);
		this.mapWidth = astarData.getCellWidth();
		this.mapHeight = astarData.getCellHeight();
	}

	public AStarCell getCell(int x, int y) {
		return cellMgr.getCell(tileId, x, y);
	}
	
	public int getMapWith() {
		return mapWidth;
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
		
		if (cell.getX() < (mapWidth - 1) && cell.getY() > 0) {// down right
			AStarCell neighbor = getCell(cell.getX() + 1, cell.getY() - 1);
			if(neighbor != null) {
				neighborList.add(neighbor);
			}
		}
		
		if (cell.getX() < (mapWidth - 1)) {// right
			AStarCell neighbor = getCell(cell.getX() + 1, cell.getY());
			if(neighbor != null) {
				neighborList.add(neighbor);
			}
		}
		
		if (cell.getX() < (mapWidth - 1) && cell.getY() < (mapHeight - 1)) { // up right
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
