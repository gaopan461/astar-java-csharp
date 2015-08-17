﻿using System;
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
        private static int mapWidth = 50;
        private static int mapHeight = 50;

        public static void run(String[] args) 
        {
		    Point a = new Point(49, 49);
		    Point b = new Point(0, 0);
		
		    Logger log = new Logger();
		    Stopwatch s = new Stopwatch();
		
		    log.addToLog("Initializing "+mapWidth+"x"+mapHeight+" map...");
		    AStarMap map = new AStarMap(mapWidth, mapHeight);
		
		    log.addToLog("Generating Bresenham's Line from "+a.x+","+a.y+" to "+b.x+","+b.y+"...");
		    s.Start();
		    List<Point> line = BresenhamsLine.getCellsOnLine(a, b);
		    s.Stop();
		    log.addToLog("Generation took " + s.ElapsedMilliseconds + " ms");
		
		    String str = "";
		    foreach(Point point in line) 
            {
			    str = str+"("+point.x+","+point.y+") ";
		    }
		    log.addToLog("Line is:" + str);
		
		    log.addToLog("Writing line to map...");
		    foreach(Point point in line) 
            {
			    map.setObstacle(point.x, point.y, true);
		    }
		    log.addToLog("Printing map...");
		    new PrintMap(map, new List<Point>());
	    }
    }
}
