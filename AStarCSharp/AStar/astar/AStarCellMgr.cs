using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AStar.astar
{
    class AStarCellMgr
    {
        private Dictionary<String, Dictionary<int, AStarCell>> cells = new Dictionary<String, Dictionary<int, AStarCell>>();
	
	    public AStarCell getCell(String tileId, int x, int y)
        {
            if(!cells.ContainsKey(tileId))
            {
                cells.Add(tileId, loadTileCells(tileId));
            }

            Dictionary<int, AStarCell> cellsOfTile = cells[tileId];
		
		    int cellId = AStarMap.makeCellId(x, y);

            if(cellsOfTile.ContainsKey(cellId))
            {
                return cellsOfTile[cellId];
            }
            else
            {
                return null;
            }
	    }
	
	    private Dictionary<int, AStarCell> loadTileCells(String tileId)
        {
            Dictionary<int, AStarCell> cellsOfTile = new Dictionary<int, AStarCell>();
		
		    AStarData astarData = AStarDataMgr.getAstarData(tileId);
		    for(int y = 0; y < astarData.getHeightInCells(); ++y)
            {
			    for(int x = 0; x < astarData.getWidthInCells(); ++x)
                {
				    int obstacle = astarData.getObstacle(x, y);
                    if (obstacle == AStarData.OBSTACLE_VALUE)
                    {
					    continue;
				    }
				
				    AStarCell cell = new AStarCell(x, y);
				    int cellId = AStarMap.makeCellId(x, y);
				    cellsOfTile.Add(cellId, cell);
			    }
		    }
		    return cellsOfTile;
	    }
    }
}
