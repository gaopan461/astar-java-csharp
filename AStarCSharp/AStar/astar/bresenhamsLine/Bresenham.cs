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
	 * @param cell1 the starting point
	 * @param cell2 the finishing point
	 * @return the line as a list of array elements
	 */
        public static List<Point> getCellsOnLine(Point cell1, Point cell2)
        {

            List<Point> line = new List<Point>();

            int dx = Math.Abs(cell2.x - cell1.x);
            int dy = Math.Abs(cell2.y - cell1.y);

            int sx = cell1.x < cell2.x ? 1 : -1;
            int sy = cell1.y < cell2.y ? 1 : -1;

            int err = dx - dy;
            int e2;
            int currentX = cell1.x;
            int currentY = cell1.y;

            while (true)
            {
                line.Add(new Point(currentX, currentY));

                if (currentX == cell2.x && currentY == cell2.y)
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
