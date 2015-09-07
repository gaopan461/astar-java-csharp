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
            // exception: start is obstacle. Now just return start
            if (AStarCell.isObstacle(map.getCell(start.x, start.y)))
            {
                return start;
            }

            int dx = Math.Abs(goal.x - start.x);
            int dy = Math.Abs(goal.y - start.y);

            int sx = start.x < goal.x ? 1 : -1;
            int sy = start.y < goal.y ? 1 : -1;

            int err = dx - dy;
            int e2;
            int currentX = start.x;
            int currentY = start.y;

            Point hitPoint = new Point(currentX, currentY);
            while (true)
            {
                AStarCell cell = map.getCell(currentX, currentY);
                if (AStarCell.isObstacle(cell))
                {
                    break;
                }
                else
                {
                    hitPoint.x = currentX;
                    hitPoint.y = currentY;
                }

                if (currentX == goal.x && currentY == goal.y)
                {
                    break;
                }

                e2 = 2 * err;
                if (e2 > -1 * dy)
                {
                    err = err - dy;
                    currentX = currentX + sx;
                }

                if (e2 < dx)
                {
                    err = err + dx;
                    currentY = currentY + sy;
                }
            }

            return hitPoint;
	    }

        public List<Point> findStraightPath(Point start, Point goal)
        {
            List<Point> waypoints;

            // optimized, check can straight pass
            Point hitPoint = raycast(start, goal);
            if (hitPoint.Equals(goal))
            {
                waypoints = new List<Point>();
                waypoints.Add(start);
                waypoints.Add(goal);
                return waypoints;
            }

            log.addToLog("AStar Heuristic initializing...");
            AStarHeuristic heuristic = new DiagonalHeuristic();

            log.addToLog("AStar initializing...");
            AStar aStar = new AStar(map, heuristic);

            log.addToLog("Calculating shortest path with AStar...");
            List<Point> shortestPath = aStar.calcShortestPath(start.x, start.y, goal.x, goal.y);
            if (shortestPath == null || shortestPath.Count == 0)
            {
                return null;
            }

            log.addToLog("Calculating optimized waypoints...");
            s.Start();
            waypoints = calcStraightPath(shortestPath);
            s.Stop();
            log.addToLog("Time to calculate waypoints: " + s.ElapsedMilliseconds + " ms");

            return waypoints;
        }

        private List<Point> calcStraightPath(List<Point> shortestPath)
        {
            if (shortestPath == null || shortestPath.Count == 0)
            {
                return null;
            }

            List<Point> waypoints = new List<Point>();

            Point p1 = shortestPath[0];
            int p1Number = 0;
            waypoints.Add(p1);

            Point p2 = shortestPath[1];
            int p2Number = 1;

            while (!p2.Equals(shortestPath[shortestPath.Count - 1]))
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
            Point hitPoint = raycast(a, b);
            if (hitPoint.Equals(b))
            {
                return true;
            }
            else
            {
                return false;
            }
	    }
    }
}
