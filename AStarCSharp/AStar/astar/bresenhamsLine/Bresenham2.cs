using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AStar.astar.bresenhamsLine
{
    class Bresenham2
    {
        private static Point toPoint(float x, float y)
        {
            return new Point((int)Math.Floor(x), (int)Math.Floor(y));
        }

        public static List<Point> getCellsOnLine(Point start, Point goal)
        {
            List<Point> line = new List<Point>();
            line.Add(start);

            if (start.Equals(goal))
            {
                return line;
            }

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

                line.Add(newCell);
                lastPassedx = nextx;
                lastPassedy = nexty;
            }

            return line;
        }
    }
}
