package pathFinding;

import java.util.HashMap;
import java.util.Map;

import pathFinding.core.AStarCell;

public class AStarCellMgr {
	private Map<String, Map<Integer, AStarCell>> cells = new HashMap<>();
	
	public static int makeCellId(int x, int y) {
		return y << 16 | x;
	}
	
	public AStarCell getCell(String tileId, int x, int y) {
		int cellId = makeCellId(x, y);
		Map<Integer, AStarCell> cellsOfTile = cells.get(tileId);
		if(cellsOfTile == null) {
			cellsOfTile = loadTileCells(tileId);
			cells.put(tileId, cellsOfTile);
		}
		
		return cellsOfTile.get(cellId);
	}
	
	private Map<Integer, AStarCell> loadTileCells(String tileId) {
		Map<Integer, AStarCell> cellsOfTile = new HashMap<>();
		
		AStarData astarData = AStarDataMgr.getAstarData(tileId);
		for(int y = 0; y < astarData.getCellHeight(); ++y) {
			for(int x = 0; x < astarData.getCellWidth(); ++x) {
				int obstacle = astarData.getObstacle(x, y);
				if(obstacle == 1) {
					continue;
				}
				
				AStarCell cell = new AStarCell(x, y);
				int cellId = makeCellId(x, y);
				cellsOfTile.put(cellId, cell);
			}
		}
		return cellsOfTile;
	}
}
