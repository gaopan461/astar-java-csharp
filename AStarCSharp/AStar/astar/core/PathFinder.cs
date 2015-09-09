﻿using AStar.astar.graphics;
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

        private static Point toPoint(float x, float y)
        {
            return new Point((int)Math.Floor(x), (int)Math.Floor(y));
        }

        public Point raycast(Point start, Point goal) 
        {
            // exception: start is obstacle. Now just return start
            if (AStarCell.isObstacle(map.getCell(start.x, start.y)))
            {
                return start;
            }

            if (start.Equals(goal))
            {
                return start;
            }

            List<Point> line = new List<Point>();
            line.Add(start);

            float startx = start.x + 0.5f;
            float starty = start.y + 0.5f;
            float goalx = goal.x + 0.5f;
            float goaly = goal.y + 0.5f;

            float diffx = goalx - startx;
            float diffy = goaly - starty;
            float distanceSquare = diffx * diffx + diffy * diffy;
            float distance = (float)Math.Sqrt(distanceSquare);

            float dirx = diffx / distance;
            float diry = diffy / distance;

            float lastPassedx = startx;
            float lastPassedy = starty;
            Point hitPoint = new Point(start.x, start.y);
            while (true)
            {
                float nextx = 0;
                float nexty = 0;

                Point coord = toPoint(lastPassedx, lastPassedy);
                float dx;
                float dy;
                if (dirx > 0)
                {
                    if (diry > 0)
                    {
                        float cx = coord.x + 1;
                        float cy = coord.y + 1;
                        dx = cx - lastPassedx;
                        dy = cy - lastPassedy;
                    }
                    else
                    {
                        float cx = coord.x + 1;
                        float cy = coord.y;
                        dx = cx - lastPassedx;
                        dy = lastPassedy - cy;
                    }
                }
                else
                {
                    if (diry > 0)
                    {
                        float cx = coord.x;
                        float cy = coord.y + 1;
                        dx = lastPassedx - cx;
                        dy = cy - lastPassedy;
                    }
                    else
                    {
                        float cx = coord.x;
                        float cy = coord.y;
                        dx = lastPassedx - cx;
                        dy = lastPassedy - cy;
                    }
                }

                float factorx = Math.Abs(dx / dirx);
                float factory = Math.Abs(dy / diry);

                if (float.IsInfinity(factorx))
                {
                    factorx = 0.0f;
                }

                if (float.IsInfinity(factory))
                {
                    factory = 0.0f;
                }

                if (factory == 0.0f)
                {
                    nextx = lastPassedx + dirx * factorx;
                    nexty = lastPassedy + diry * factorx;
                }
                else if (factorx == 0.0f)
                {
                    nextx = lastPassedx + dirx * factory;
                    nexty = lastPassedy + diry * factory;
                }
                else if (factorx < factory)
                {
                    nextx = lastPassedx + dirx * factorx;
                    nexty = lastPassedy + diry * factorx;
                }
                else
                {
                    nextx = lastPassedx + dirx * factory;
                    nexty = lastPassedy + diry * factory;
                }

                Point newCell;
                do
                {
                    nextx += dirx * 0.001f;
                    nexty += diry * 0.001f;
                    newCell = toPoint(nextx, nexty);
                } while (newCell.Equals(coord));

                float dis = (nextx - startx) * (nextx - startx) + (nexty - starty) * (nexty - starty);
                if (dis >= distanceSquare)
                {
                    break;
                }

                if (AStarCell.isObstacle(map.getCell(newCell.x, newCell.y)))
                {
                    break;
                }
                else
                {
                    hitPoint.x = newCell.x;
                    hitPoint.y = newCell.y;

                    if (newCell.Equals(goal))
                    {
                        break;
                    }
                }

                lastPassedx = nextx;
                lastPassedy = nexty;
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
            if (shortestPath == null || shortestPath.Count <= 0)
            {
                return shortestPath;
            }

            List<Point> waypoints = new List<Point>();

            Point p1 = shortestPath[0];
            int p1Number = 0;
            waypoints.Add(p1);

            int p2Number = 1;
            do
            {
                Point p2 = shortestPath[p2Number];
                if (!lineClear(p1, p2))
                {
                    p1Number = p2Number - 1;
                    p1 = shortestPath[p1Number];
                    waypoints.Add(p1);
                    log.addToLog("Got waypoint: " + p1.ToString());
                }
                p2Number++;
            } while (p2Number < shortestPath.Count);
            waypoints.Add(shortestPath[p2Number - 1]);

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
