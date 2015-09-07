package pathFinding;

import java.util.HashMap;
import java.util.Map;

import pathFinding.core.AStarCell;
import pathFinding.core.AStarMap;

/**
 * @author g
 * 对于多地块场景，游戏中，同一线程下的所有相同场景不能共享CellMgr中的Cell
 * 原因是在同一场景中，可能包括多块相同的地块，这样在A*寻路时会产生冲突
 * 故这里会拷贝一份CellMgr中的Cell
 *
 */
public class AStarMultiTileMap extends AStarMap {
	private Map<Integer, AStarCell> cells = new HashMap<>();
	
	private int widthInTiles;
	private int heigthInTiles;
	private String[][] tiles;
	
	private int widthInCellsPerTile;
	private int heightInCellsPerTile;
	
	public AStarMultiTileMap(String[][] tiles, int widthInTiles, int heigthInTiles) {
		this.tiles = tiles;
		this.widthInTiles = widthInTiles;
		this.heigthInTiles = heigthInTiles;
		
		AStarData firstData = AStarDataMgr.getAstarData(tiles[0][0]);
		this.widthInCellsPerTile = firstData.getWidthInCells();
		this.heightInCellsPerTile = firstData.getHeightInCells();
		this.cellSize = firstData.getCellSize();
		
		this.widthInCells = widthInTiles * widthInCellsPerTile;
		this.heightInCells = heigthInTiles * heightInCellsPerTile;
		
		checkTiles();
		
		loadTiles();
	}

	@Override
	public AStarCell getCell(int x, int y) {
		int cellId = makeCellId(x, y);
		return cells.get(cellId);
	}
	
	private void checkTiles() {
		for(int h = 0; h < heigthInTiles; ++h) {
			for(int w = 0; w < widthInTiles; ++w) {
				AStarData astarData = AStarDataMgr.getAstarData(tiles[h][w]);
				if(astarData == null) {
					throw new RuntimeException("tile not exist:" + tiles[h][w]);
				}
				
				if(astarData.getWidthInCells() != widthInCellsPerTile
						|| astarData.getHeightInCells() != heightInCellsPerTile) {
					throw new RuntimeException("tile size invalid:" + tiles[h][w]);
				}
			}
		}
	}
	
	private void loadTiles() {
		for(int tileY = 0; tileY < heigthInTiles; ++tileY) {
			for(int tileX = 0; tileX < widthInTiles; ++tileX) {
				String tileId = tiles[tileY][tileX];
				AStarData astarData = AStarDataMgr.getAstarData(tileId);
				for(int cellY = 0; cellY < heightInCellsPerTile; ++cellY) {
					for(int cellX = 0; cellX < widthInCellsPerTile; ++cellX) {
						int obstacle = astarData.getObstacle(cellX, cellY);
						if(obstacle == 1) {
							continue;
						}
						
						int x = tileX * widthInCellsPerTile + cellX;
						int y = tileY * heightInCellsPerTile + cellY;
						int cellId = makeCellId(x, y);
						cells.put(cellId, new AStarCell(x, y));
					}
				}
			}
		}
	}

	@Override
	public float getHeight(int x, int y) {
		int tileX = x / widthInCellsPerTile;
		int tileY = y / heightInCellsPerTile;
		int cellX = x % widthInCellsPerTile;
		int cellY = y % heightInCellsPerTile;
		
		if(tileX >= widthInTiles || tileY >= heigthInTiles) {
			throw new RuntimeException("pos invalid, x=" + x + ",y=" + y);
		}
		
		String tileId = tiles[tileY][tileX];
		return AStarDataMgr.getAstarData(tileId).getHeight(cellX, cellY);
	}
	
}
