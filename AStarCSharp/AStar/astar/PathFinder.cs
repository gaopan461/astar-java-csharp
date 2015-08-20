using AStar.astar.bresenhamsLine;
using AStar.astar.graphics;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AStar.astar
{
    class PathFinder
    {
        private AStarMap map;
        private Logger log = new Logger();
        private Stopwatch s = new Stopwatch();

        public PathFinder(AStarMap map)
        {
            this.map = map;
        }

        public Point raycast(Point start, Point goal) 
        {
		    List<Point> pointsOnLine = Bresenham.getCellsOnLine(start, goal);
		
		    Point hitPoint = (Point) start.Clone();
		    foreach(Point p in pointsOnLine) {
                AStarCell cell = map.getCell(p.x, p.y);
			    if(cell == null || cell.isObstacle()) {
				    break;
			    } else {
				    hitPoint = p;
			    }
		    }
		    return hitPoint;
	    }

        public List<Point> findStraightPath(Point start, Point goal)
        {
            log.addToLog("AStar Heuristic initializing...");
            AStarHeuristic heuristic = new DiagonalHeuristic();

            log.addToLog("AStar initializing...");
            AStar aStar = new AStar(map, heuristic);

            log.addToLog("Calculating shortest path with AStar...");
            List<Point> shortestPath = aStar.calcShortestPath(start.x, start.y, goal.x, goal.y);

            //log.addToLog("Printing map of shortest path...");
            //new PrintMap(map, shortestPath);

            log.addToLog("Calculating optimized waypoints...");
            s.Start();
            List<Point> waypoints = calcStraightPath(shortestPath);
            s.Stop();
            log.addToLog("Time to calculate waypoints: " + s.ElapsedMilliseconds + " ms");

            return waypoints;
        }

        private List<Point> calcStraightPath(List<Point> shortestPath)
        {
            List<Point> waypoints = new List<Point>();

            Point p1 = shortestPath[0];
            int p1Number = 0;
            waypoints.Add(p1);

            Point p2 = shortestPath[1];
            int p2Number = 1;

            while (!p2.Equals(shortestPath[shortestPath.Count() - 1]))
            {
                if (lineClear(p1, p2))
                {
                    //make p2 the next point in the path
                    p2Number++;
                    p2 = shortestPath[p2Number];
                }
                else
                {
                    p1Number = p2Number - 1;
                    p1 = shortestPath[p1Number];
                    waypoints.Add(p1);
                    log.addToLog("Got waypoint: " + p1.ToString());
                    p2Number++;
                    p2 = shortestPath[p2Number];
                }
            }
            waypoints.Add(p2);

            return waypoints;
        }

        private bool lineClear(Point a, Point b)
        {
		    List<Point> pointsOnLine = Bresenham.getCellsOnLine(a, b);
		    foreach(Point p in pointsOnLine) 
            {
                AStarCell cell = map.getCell(p.x, p.y);
			    if(cell == null || cell.isObstacle()) 
                {
				    return false;
			    }
		    }
		    return true;
	    }
    }
}
