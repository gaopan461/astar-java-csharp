package pathFinding;

import pathFinding.core.AStarCell;
import pathFinding.core.AStarMap;

/**
 * @author g
 * 对于单地块场景，游戏中，同一线程下的所有相同场景可以共享CellMgr中的Cell
 *
 */
public class AStarSingleTileMap extends AStarMap {
	private String tileId;
	
	private AStarCellMgr cellMgr;
	
	public AStarSingleTileMap(String tileId, AStarCellMgr cellMgr) {
		this.cellMgr = cellMgr;
		AStarData astarData = AStarDataMgr.getAstarData(tileId);
		this.widthInCells = astarData.getWidthInCells();
		this.heightInCells = astarData.getHeightInCells();
		this.cellSize = astarData.getCellSize();
		
		this.tileId = tileId;
		this.cellMgr = cellMgr;
	}

	@Override
	public AStarCell getCell(int x, int y) {
		return cellMgr.getCell(tileId, x, y);
	}

	@Override
	public float getHeight(int x, int y) {
		return AStarDataMgr.getAstarData(tileId).getHeight(x, y);
	}

}
