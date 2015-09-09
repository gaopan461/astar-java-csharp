using AStar.astar.bresenhamsLine;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AStar.astar.tests
{
    class TestBresenham
    {
        public static void run(String[] args)
        {
            Bresenham2.getCellsOnLine(new Point(0, 0), new Point(2, 1));
        }
    }
}
