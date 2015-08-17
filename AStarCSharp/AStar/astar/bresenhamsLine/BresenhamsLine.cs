using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AStar.astar.bresenhamsLine
{
    class BresenhamsLine
    {
        public static List<Point> getCellsOnLine(Point p1, Point p2)
        {

            Point a = (Point)p1.Clone();
            Point b = (Point)p2.Clone();

            List<Point> cellsOnLine = new List<Point>();

            bool steep = Math.Abs(b.y - a.y) > Math.Abs(b.x - a.x);

            if (steep)
            {
                // swap(a.x, a.y)
                int temp;
                temp = a.x;
                a.x = a.y;
                a.y = temp;
                // swap(b.x, b.y)
                temp = a.x;
                b.x = b.y;
                b.y = temp;
            }
            if (a.x > b.x)
            {
                // swap(a.x, b.x)
                int temp;
                temp = a.x;
                a.x = b.x;
                b.x = temp;
                // swap(a.y, b.y)
                temp = a.y;
                a.y = b.y;
                b.y = temp;
            }

            int deltaX = b.x - a.x;
            int deltaY = Math.Abs(b.y - a.y);
            int error = deltaX / 2;

            int yStep;
            int y = a.y;
            if (a.y < b.y)
                yStep = 1;
            else
                yStep = -1;

            for (int x = a.x; x <= b.x; x++)
            {
                if (steep)
                    cellsOnLine.Add(new Point(y, x));
                else
                    cellsOnLine.Add(new Point(x, y));
                error = error - deltaY;
                if (error < 0)
                {
                    y = y + yStep;
                    error = error + deltaX;
                }
            }

            return cellsOnLine;

            //		function line(x0, x1, y0, y1)
            //	     boolean steep := abs(y1 - y0) > abs(x1 - a.x)
            //	     if steep then
            //	         swap(a.x, y0)
            //	         swap(x1, y1)
            //	     if a.x > x1 then
            //	         swap(a.x, x1)
            //	         swap(y0, y1)
            //	     int deltax := x1 - a.x
            //	     int deltay := abs(y1 - y0)
            //	     int error := deltax / 2
            //	     int ystep
            //	     int y := y0
            //	     if y0 < y1 then ystep := 1 else ystep := -1
            //	     for x from a.x to b.x
            //	         if steep then plot(y,x) else plot(x,y)
            //	         error := error - deltay
            //	         if error < 0 then
            //	             y := y + ystep
            //	             error := error + deltax

        }
    }
}
