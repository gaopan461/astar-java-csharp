using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AStar.astar.bresenhamsLine
{
    /**
     * Implementation of the Bresenham line algorithm.
     * @author fragkakis
     *
     */
    class Bresenham
    {
        /**
	 * Returns the list of array elements that comprise the line. 
	 * @param start the starting point
	 * @param goal the finishing point
	 * @return the line as a list of array elements
	 */
        public static List<Point> getCellsOnLine(Point start, Point goal)
        {

            List<Point> line = new List<Point>();

            int dx = Math.Abs(goal.x - start.x);
            int dy = Math.Abs(goal.y - start.y);

            int sx = start.x < goal.x ? 1 : -1;
            int sy = start.y < goal.y ? 1 : -1;

            int err = dx - dy;
            int e2;
            int currentX = start.x;
            int currentY = start.y;

            while (true)
            {
                line.Add(new Point(currentX, currentY));

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

            return line;
        }
    }
}
