using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Collections;

namespace AStar
{
    class AStarCell : IComparable<AStarCell>, IEquatable<AStarCell>
    {
        /* Cells that this is connected to */
        private AStarMap map;
        private float distanceFromStart;
        private float heuristicDistanceFromGoal;
        private AStarCell previousCell;
        private int x;
        private int y;

        private bool obstacle;
        private bool start;
        private bool goal;

        public AStarCell(int x, int y, AStarMap map)
        {
            this.x = x;
            this.y = y;
            this.map = map;
            this.distanceFromStart = Int32.MaxValue;
            this.obstacle = false;
            this.start = false;
            this.goal = false;
        }

        public AStarCell(int x, int y, AStarMap map, bool visited, int distanceFromStart, bool isObstical, bool isStart, bool isGoal)
        {
            this.x = x;
            this.y = y;
            this.map = map;
            this.distanceFromStart = distanceFromStart;
            this.obstacle = isObstical;
            this.start = isStart;
            this.goal = isGoal;
        }

        public List<AStarCell> getNeighborList()
        {
            List<AStarCell> neighborList = new List<AStarCell>();
            if (y > 0)
            {// down
                neighborList.Add(map.getCell(x, (y - 1)));
            }
            if (y > 0 && x < (map.getMapWith() - 1))
            {// down right
                neighborList.Add(map.getCell(x + 1, y - 1));
            }
            if (x < (map.getMapWith() - 1))
            {// right
                neighborList.Add(map.getCell(x + 1, y));
            }
            if (x < (map.getMapWith() - 1) && y < (map.getMapHeight() - 1))
            { // up right
                neighborList.Add(map.getCell(x + 1, y + 1));
            }
            if (y < (map.getMapHeight() - 1))
            {// up
                neighborList.Add(map.getCell(x, y + 1));
            }
            if (x > 0 && y < (map.getMapHeight() - 1))
            {// up left
                neighborList.Add(map.getCell(x - 1, y + 1));
            }
            if (x > 0)
            {// left
                neighborList.Add(map.getCell(x - 1, y));
            }
            if (x > 0 && y > 0)
            {// down left
                neighborList.Add(map.getCell(x - 1, y - 1));
            }
            return neighborList;
        }

        public float getDistanceFromStart()
        {
            return distanceFromStart;
        }

        public void setDistanceFromStart(float f)
        {
            this.distanceFromStart = f;
        }

        public AStarCell getPreviousCell()
        {
            return previousCell;
        }

        public void setPreviousCell(AStarCell previousCell)
        {
            this.previousCell = previousCell;
        }

        public float getHeuristicDistanceFromGoal()
        {
            return heuristicDistanceFromGoal;
        }

        public void setHeuristicDistanceFromGoal(float heuristicDistanceFromGoal)
        {
            this.heuristicDistanceFromGoal = heuristicDistanceFromGoal;
        }

        public int getX()
        {
            return x;
        }

        public void setX(int x)
        {
            this.x = x;
        }

        public int getY()
        {
            return y;
        }

        public void setY(int y)
        {
            this.y = y;
        }

        public Point getPoint()
        {
            return new Point(x, y);
        }

        public bool isObstacle()
        {
            return obstacle;
        }

        public void setObstacle(bool obstacle)
        {
            this.obstacle = obstacle;
        }

        public bool isStart()
        {
            return start;
        }

        public void setStart(bool start)
        {
            this.start = start;
        }

        public bool isGoal()
        {
            return goal;
        }

        public void setGoal(bool goal)
        {
            this.goal = goal;
        }

        public bool equals(AStarCell cell)
        {
            return (cell.x == x) && (cell.y == y);
        }

        public int CompareTo(AStarCell otherCell)
        {
            float thisTotalDistanceFromGoal = heuristicDistanceFromGoal + distanceFromStart;
            float otherTotalDistanceFromGoal = otherCell.getHeuristicDistanceFromGoal() + otherCell.getDistanceFromStart();

            if(Math.Abs(thisTotalDistanceFromGoal - otherTotalDistanceFromGoal) < 0.01)
            {
                return 0;
            }
            else if (thisTotalDistanceFromGoal < otherTotalDistanceFromGoal)
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }

        public bool Equals(AStarCell other)
        {
            return (x == other.x) && (y == other.y);
        }
    }
}
