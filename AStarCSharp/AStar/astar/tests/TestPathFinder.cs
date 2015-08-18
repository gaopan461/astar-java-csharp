using AStar.astar.graphics;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AStar.astar.tests
{
    class TestPathFinder
    {
        private static int startX = 50;
        private static int startY = 12;
        private static int goalX = 158;
        private static int goalY = 110;

        private static AStarMapData mapData = new AStarMapData();

        public static void run(String[] args)
        {
            Logger log = new Logger();
            Stopwatch s = new Stopwatch();

            log.addToLog("Map initializing...");
            AStarMap map = new AStarMap(mapData.getMapWidth(), mapData.getMapHeight(), mapData.getObstacleMap());

            s.Start();

            PathFinder pathfinder = new PathFinder(map);
            Point start = new Point(startX, startY);
            Point goal = new Point(goalX, goalY);
            List<Point> optimizedWaypoints = pathfinder.findStraightPath(start, goal);

            s.Stop();
            log.addToLog("Total pathfinding took: " + s.ElapsedMilliseconds + " ms");

            log.addToLog("Printing map of optimized path...");
            new PrintMap(map, optimizedWaypoints);
        }
    }
}
