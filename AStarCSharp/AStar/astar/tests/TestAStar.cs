using AStar.astar.graphics;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AStar.astar.tests
{
    class TestAStar
    {
        private static int startX = 50;
        private static int startY = 12;
        private static int goalX = 110;
        private static int goalY = 75;

        private static AStarMapData mapData = new AStarMapData();

        public static void run(String[] args)
        {
            Logger log = new Logger();
            Stopwatch s = new Stopwatch();

            log.addToLog("Map initializing...");
            AStarMap map = new AStarMap(mapData.getMapWidth(), mapData.getMapHeight(), mapData.getObstacleMap());

            log.addToLog("Heuristic initializing...");
            //AStarHeuristic heuristic = new ClosestHeuristic();
            AStarHeuristic heuristic = new DiagonalHeuristic();

            log.addToLog("AStar initializing...");
            AStar aStar = new AStar(map, heuristic);

            log.addToLog("Calculating shortest path...");
            s.Start();
            List<Point> shortestPath = aStar.calcShortestPath(startX, startY, goalX, goalY);
            s.Stop();

            log.addToLog("Time to calculate path in milliseconds: " + s.ElapsedMilliseconds);

            log.addToLog("Printing map of shortest path...");
            new PrintMap(map, shortestPath);
        }
    }
}
