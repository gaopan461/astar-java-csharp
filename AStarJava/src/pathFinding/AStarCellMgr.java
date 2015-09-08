package pathFinding;

import java.util.HashMap;
import java.util.Map;

import pathFinding.core.AStarCell;
import pathFinding.core.AStarMap;

public class AStarCellMgr {
	private Map<String, Map<Integer, AStarCell>> cells = new HashMap<>();
	
	public AStarCell getCell(String tileId, int x, int y) {
		Map<Integer, AStarCell> cellsOfTile = cells.get(tileId);
		if(cellsOfTile == null) {
			cellsOfTile = loadTileCells(tileId);
			cells.put(tileId, cellsOfTile);
		}
		
		int cellId = AStarMap.makeCellId(x, y);
		return cellsOfTile.get(cellId);
	}
	
	private Map<Integer, AStarCell> loadTileCells(String tileId) {
		Map<Integer, AStarCell> cellsOfTile = new HashMap<>();
		
		AStarData astarData = AStarDataMgr.getAstarData(tileId);
		for(int y = 0; y < astarData.getHeightInCells(); ++y) {
			for(int x = 0; x < astarData.getWidthInCells(); ++x) {
				int obstacle = astarData.getObstacle(x, y);
				if(obstacle == AStarData.OBSTACLE_VALUE) {
					continue;
				}
				
				AStarCell cell = new AStarCell(x, y);
				int cellId = AStarMap.makeCellId(x, y);
				cellsOfTile.put(cellId, cell);
			}
		}
		return cellsOfTile;
	}
}
