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

        public AStarCell(AStarMap map, int x, int y)
        {
            this.map = map;
            this.x = x;
            this.y = y;
            this.distanceFromStart = Int32.MaxValue;
        }

        public List<AStarCell> getNeighborList()
        {
            List<AStarCell> neighborList = new List<AStarCell>();
            if (y > 0)
            {// down
                AStarCell cell = map.getCell(x, (y - 1));
                if(cell != null)
                {
                    neighborList.Add(cell);
                }
            }

            if (y > 0 && x < (map.getMapWith() - 1))
            {// down right
                AStarCell cell = map.getCell(x + 1, y - 1);
                if(cell != null)
                {
                    neighborList.Add(cell);
                }
            }

            if (x < (map.getMapWith() - 1))
            {// right
                AStarCell cell = map.getCell(x + 1, y);
                if(cell != null)
                {
                    neighborList.Add(cell);
                }
            }

            if (x < (map.getMapWith() - 1) && y < (map.getMapHeight() - 1))
            { // up right
                AStarCell cell = map.getCell(x + 1, y + 1);
                if(cell != null)
                {
                    neighborList.Add(cell);
                }
            }

            if (y < (map.getMapHeight() - 1))
            {// up
                AStarCell cell = map.getCell(x, y + 1);
                if(cell != null)
                {
                    neighborList.Add(cell);
                }
            }

            if (x > 0 && y < (map.getMapHeight() - 1))
            {// up left
                AStarCell cell = map.getCell(x - 1, y + 1);
                if(cell != null)
                {
                    neighborList.Add(cell);
                }
            }

            if (x > 0)
            {// left
                AStarCell cell = map.getCell(x - 1, y);
                if(cell != null)
                {
                    neighborList.Add(cell);
                }
            }

            if (x > 0 && y > 0)
            {// down left
                AStarCell cell = map.getCell(x - 1, y - 1);
                if(cell != null)
                {
                    neighborList.Add(cell);
                }
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

        public int getY()
        {
            return y;
        }

        public Point getPoint()
        {
            return new Point(x, y);
        }

        public bool isObstacle()
        {
            return false;
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
