using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AStar
{
    /**
     * The AreaMap holds information about the With, Height, 
     * Start position, Goal position and Obstacles on the map.
     * A place on the map is referred to by it's (x,y) coordinates, 
     * where (0,0) is the upper left corner, and x is horizontal and y is vertical.
     */
    class AStarMap
    {
        private static float SQRT2 = (float)Math.Sqrt(2);

        private int mapWith;
        private int mapHeight;
        private List<List<AStarCell>> map;
        private int startLocationX = 0;
        private int startLocationY = 0;
        private int goalLocationX = 0;
        private int goalLocationY = 0;
        private int[][] obstacleMap;

        private Logger log = new Logger();

        /**
         * Class constructor specifying the With and Height of a otherwise empty map.
         * (no start and goal location or obstacles)
         * @param mapWith
         * @param mapHeight
         */
        public AStarMap(int mapWith, int mapHeight)
        {
            this.mapWith = mapWith;
            this.mapHeight = mapHeight;
            this.obstacleMap = new int[this.mapHeight][];
            for (int h = 0; h < mapHeight; ++h)
            {
                this.obstacleMap[h] = new int[mapWith];
            }

            for (int h = 0; h < mapHeight; ++h)
            {
                for (int w = 0; w < mapWith; ++w)
                {
                    this.obstacleMap[h][w] = 0;
                }
            }

            createMap();
            log.addToLog("\tMap Created");
        }

        /**
         * Class constructor specifying the With, Height and Obstacles of the map.
         * (no start and goal location)
         * The Obstacle 2D array map can be any With and Height
         * @param mapWith		the with of the map as int
         * @param mapHeight		the Height of the map as int
         * @param obstacleMap	a 2D int array map of the obstacles on the map. '1' is obstacle, '0' is not.
         */
        public AStarMap(int mapWith, int mapHeight, int[][] obstacleMap)
        {
            this.mapWith = mapWith;
            this.mapHeight = mapHeight;
            this.obstacleMap = new int[this.mapHeight][];
            for (int h = 0; h < mapHeight; ++h)
            {
                this.obstacleMap[h] = new int[mapWith];
            }

            for (int h = 0; h < mapHeight; ++h)
            {
                for (int w = 0; w < mapWith; ++w)
                {
                    this.obstacleMap[h][w] = obstacleMap[h][w];
                }
            }

            createMap();
            log.addToLog("\tMap Created");
        }

        /**
         * Sets up the Cells of the map with the With and Height specified in the constructor
         * or set methods.
         */
        private void createMap()
        {
            AStarCell cell;
            map = new List<List<AStarCell>>();
            for (int x = 0; x < mapWith; x++)
            {
                map.Add(new List<AStarCell>());
                for (int y = 0; y < mapHeight; y++)
                {
                    cell = new AStarCell(x, y, this);
                    try
                    {
                        if (obstacleMap[y][x] == 1)
                            cell.setObstacle(true);
                    }
                    catch (Exception ) { }
                    map[x].Add(cell);
                }
            }
        }

        public void setObstacle(int x, int y, bool isObstical)
        {
            map[x][y].setObstacle(isObstical);
        }

        public AStarCell getCell(int x, int y)
        {
            return map[x][y];
        }

        public void setStartLocation(int x, int y)
        {
            map[startLocationX][startLocationY].setStart(false);
            map[x][y].setStart(true);
            startLocationX = x;
            startLocationY = y;
        }

        public void setGoalLocation(int x, int y)
        {
            map[goalLocationX][goalLocationY].setGoal(false);
            map[x][y].setGoal(true);
            goalLocationX = x;
            goalLocationY = y;
        }

        public int getStartLocationX()
        {
            return startLocationX;
        }

        public int getStartLocationY()
        {
            return startLocationY;
        }

        public AStarCell getStartCell()
        {
            return map[startLocationX][startLocationY];
        }

        public int getGoalLocationX()
        {
            return goalLocationX;
        }

        public int getGoalLocationY()
        {
            return goalLocationY;
        }

        public Point getGoalPoint()
        {
            return new Point(goalLocationX, goalLocationY);
        }

        /**
         * @return Cell	The Goal Cell
         * @see Cell
         */
        public AStarCell getGoalCell()
        {
            return map[goalLocationX][goalLocationY];
        }

        /**
         * Determine the distance between two neighbor Cells 
         * as used by the AStar algorithm.
         * 
         * @param cell1 any Cell
         * @param cell2 any of cell1's neighbors
         * @return Float - the distance between the two neighbors
         */
        public float getDistanceBetween(AStarCell cell1, AStarCell cell2)
        {
            //if the cells are on top or next to each other, return 1
            if (cell1.getX() == cell2.getX() || cell1.getY() == cell2.getY())
            {
                return 1;//*(mapHeight+mapWith);
            }
            else
            { //if they are diagonal to each other return diagonal distance: sqrt(1^2+1^2)
                return SQRT2;//*(mapHeight+mapWith);
            }
        }

        public int getMapWith()
        {
            return mapWith;
        }
        public int getMapHeight()
        {
            return mapHeight;
        }

        /**
         * Removes all the map information about start location, goal location and obstacles.
         * Then remakes the map with the original With and Height. 
         */
        public void clear()
        {
            startLocationX = 0;
            startLocationY = 0;
            goalLocationX = 0;
            goalLocationY = 0;
            createMap();
        }
    }
}
