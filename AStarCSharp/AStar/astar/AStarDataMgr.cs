using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AStar.astar
{
    class AStarDataMgr
    {
        private const String ASTAR_DIR = "\\..\\..\\Resource\\astar";
	    private const String INFO_SUFFIX = ".txt";
	
	    private static Dictionary<String, AStarData> astarDatas = new Dictionary<String, AStarData>();
	
	    public static void init()
        {
            String path = System.IO.Directory.GetCurrentDirectory() + ASTAR_DIR;
            if (!System.IO.Directory.Exists(path))
            {
                return;
            }

            String[] files = System.IO.Directory.GetFiles(path);
            foreach(String file in files)
            {
                if(!file.EndsWith(INFO_SUFFIX))
                {
                    continue;
                }

                String fileName = System.IO.Path.GetFileName(file);
                String tileId = fileName.Replace(INFO_SUFFIX, "");
                AStarData astarData = new AStarData(file);
			    astarDatas.Add(tileId, astarData);
            }
	    }
	
	    public static AStarData getAstarData(String tileId)
        {
		    return astarDatas[tileId];
	    }
    }
}
