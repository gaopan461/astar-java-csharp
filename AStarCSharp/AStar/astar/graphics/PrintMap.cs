using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AStar.astar.graphics
{
    class PrintMap
    {
        public PrintMap(AStarMap map, List<Point> shortestPath) 
        {
            StringBuilder sb = new StringBuilder();
		    AStarCell cell;
		    for(int y = 0; y < map.getMapHeight(); y++) 
            {
			    if(y == 0) 
                {
				    for (int i = 0; i <= map.getMapHeight(); i++)
                    {
                        sb.Append("-");
                    }
				    sb.AppendLine();	
			    }
			    sb.Append("|");

			    for(int x = 0; x < map.getMapWith(); x++) 
                {
				    cell = map.getCell(x, y);
				
				    if(cell.isObstacle()) 
                    {
					    sb.Append("X");
				    } 
                    else if(cell.isStart()) 
                    {
					    sb.Append("s");
				    } 
                    else if(cell.isGoal()) 
                    {
					    sb.Append("g");
				    } 
                    else if (PathContains(shortestPath, cell.getX(), cell.getY())) 
                    {
					    sb.Append("?");
				    } 
                    else 
                    {
					    sb.Append(" ");
				    }
				    if(y==map.getMapHeight())
                    {
                        sb.Append("_");
                    }
			    }

			    sb.Append("|");
			    sb.AppendLine();
		    }
		    for (int i=0; i<=map.getMapHeight(); i++)
            {
                sb.Append("-");
            }
            System.Console.SetWindowSize(System.Console.LargestWindowWidth, System.Console.LargestWindowHeight);
            System.Console.Write(sb.ToString());
	    }

        private static bool PathContains(List<Point> path, int x, int y)
        {
            foreach(Point point in path)
            {
                if (point.x == x && point.y == y)
                {
                    return true;
                }
            }

            return false;
        }
    }

}
