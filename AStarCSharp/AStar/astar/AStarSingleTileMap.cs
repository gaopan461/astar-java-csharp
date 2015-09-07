using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AStar.astar
{
    /**
     * @author g
     * 对于单地块场景，游戏中，同一线程下的所有相同场景可以共享CellMgr中的Cell
     *
     */
    class AStarSingleTileMap : AStarMap
    {
        private String tileId;
	
	    private AStarCellMgr cellMgr;
	
	    public AStarSingleTileMap(String tileId, AStarCellMgr cellMgr)
        {
		    this.cellMgr = cellMgr;
		    AStarData astarData = AStarDataMgr.getAstarData(tileId);
		    this.widthInCells = astarData.getWidthInCells();
		    this.heightInCells = astarData.getHeightInCells();
		
		    this.tileId = tileId;
		    this.cellMgr = cellMgr;
	    }

	    public override AStarCell getCell(int x, int y)
        {
		    return cellMgr.getCell(tileId, x, y);
	    }

	    public override float getHeight(int x, int y)
        {
		    return AStarDataMgr.getAstarData(tileId).getHeight(x, y);
	    }
    }
}
