using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Diagnostics;
using AStar.astar.bresenhamsLine;
using AStar.astar.graphics;

namespace AStar.astar.tests
{
    class TestBresenhamsLine
    {
        private const String TILE_ID = "normal2";

        public static void run(String[] args) 
        {
		    Point a = new Point(22, 23);
		    Point b = new Point(0, 0);
		
		    Logger log = new Logger();
		    Stopwatch s = new Stopwatch();
		
		    log.addToLog("Initializing map...");
		    AStarMap map = new AStarSingleTileMap(TILE_ID, new AStarCellMgr());
		
		    log.addToLog("Generating Bresenham's Line from "+a.x+","+a.y+" to "+b.x+","+b.y+"...");
		    s.Start();
		    List<Point> line = Bresenham.getCellsOnLine(a, b);
		    s.Stop();
		    log.addToLog("Generation took " + s.ElapsedMilliseconds + " ms");
		
		    String str = "";
		    foreach (Point point in line) {
			    str = str+"("+point.x+","+point.y+") ";
		    }
		    log.addToLog("Line is:" + str);
		
		    log.addToLog("Writing line to map...");
		    log.addToLog("Printing map...");
		    new PrintMap(map, line);
        }
    }
}
