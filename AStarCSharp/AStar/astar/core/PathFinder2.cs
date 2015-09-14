using AStar.astar.utils;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AStar.astar.core
{
    class PathFinder2
    {
        private const float FIX_DISTANCE = 0.01f;
	
	    private AStarMap map;
	    private Logger log = new Logger();
        private Stopwatch s = new Stopwatch();
	
	    public PathFinder2(AStarMap map)
        {
		    this.map = map;
	    }
	
	    public static Point posToCell(Vector2D pos, float cellSize)
        {
		    int cellX = (int)Math.Floor(pos.x / cellSize);
		    int cellY = (int)Math.Floor(pos.y / cellSize);
		    return new Point(cellX, cellY);
	    }
	
	    public static Vector2D cellToPos(Point cell, float cellSize)
        {
		    float x = (cell.x + 0.5f) * cellSize;
		    float y = (cell.y + 0.5f) * cellSize;
		    return new Vector2D(x, y);
	    }
	
	    public List<Vector2D> findStraightPath(Vector2D start, Vector2D goal, float cellSize)
        {
		    Point startCell = posToCell(start, cellSize);
		    Point goalCell = posToCell(goal, cellSize);
		
		    // optimized, check can straight pass
		    Point hitPoint = raycast(start, goal, cellSize);
		    if(hitPoint.Equals(goalCell))
            {
			    List<Vector2D> waypoints = new List<Vector2D>();
			    waypoints.Add(start);
			    waypoints.Add(goal);
			    return waypoints;
		    }
		
		    log.addToLog("AStar Heuristic initializing...");
		    AStarHeuristic heuristic = new DiagonalHeuristic();
		
		    log.addToLog("AStar initializing...");
		    AStar aStar = new AStar(map, heuristic);
		
		    log.addToLog("Calculating shortest path with AStar...");
		    List<Point> shortestPath = aStar.calcShortestPath(startCell.x, startCell.y, goalCell.x, goalCell.y);
		
		    // 终点不可达 
		    if(shortestPath == null || shortestPath.Count == 0)
            {
			    return null;
		    }

            List<Vector2D> posPath = new List<Vector2D>();
		    posPath.Add(start);
		
		    // 起点和终点在同一个cell
		    if(shortestPath.Count == 1)
            {
			    posPath.Add(goal);
			    return posPath;
		    } 
            else 
            {
			    Vector2D pos1 = cellToPos(shortestPath[0], cellSize);
			    for(int i = 1; i < shortestPath.Count; ++i)
                {
				    Vector2D pos2 = cellToPos(shortestPath[i], cellSize);
				    Vector2D dir = pos2.sub(pos1).normalize();
				    float midx = (pos1.x + pos2.x) / 2;
				    float midy = (pos1.y + pos2.y) / 2;
				    posPath.Add(new Vector2D(midx - 0.1f * dir.x, midy - 0.1f * dir.y));
				    posPath.Add(new Vector2D(midx + 0.1f * dir.x, midy + 0.1f * dir.y));
				
				    pos1 = pos2;
			    }
			
			    posPath.Add(goal);
		    }
		
		    log.addToLog("Calculating optimized waypoints...");
		    s.Start();
		    posPath = calcStraightPath(posPath, cellSize);
		    s.Stop();
		    log.addToLog("Time to calculate waypoints: " + s.ElapsedMilliseconds + " ms");
		
		    return posPath;
	    }
	
	    public List<Vector2D> calcStraightPath(List<Vector2D> shortestPath, float cellSize)
        {
		    if(shortestPath == null || shortestPath.Count <= 2)
            {
			    return shortestPath;
		    }
		
		    List<Vector2D> waypoints = new List<Vector2D>();
		
		    Vector2D p1 = shortestPath[0];
		    int p1Number = 0;
		    waypoints.Add(p1);
		
		    int p2Number = 1;
		    do
            {
			    Vector2D p2 = shortestPath[p2Number];
			    if(!lineClear(p1, p2, cellSize))
                {
				    p1Number = p2Number-1;
				    p1 = shortestPath[p1Number];
				    waypoints.Add(p1);
			    }
			    p2Number++;
		    }while(p2Number < shortestPath.Count);
		    waypoints.Add(shortestPath[p2Number - 1]);
		
		    return waypoints;
	    }
	
	    private bool lineClear(Vector2D startPos, Vector2D goalPos, float cellSize)
        {
		    Point hitPoint = raycast(startPos, goalPos, cellSize);
		    if(hitPoint.Equals(posToCell(goalPos, cellSize)))
            {
			    return true;
		    }
            else
            {
			    return false;
		    }
	    }
	
	    private static Vector2D getNextPos(Vector2D currPos, Vector2D dir, float cellSize)
        {
		    float nextx = 0;
		    float nexty = 0;
		
		    Point coord = posToCell(currPos, cellSize);
		    float dx;
		    float dy;
		    if(dir.x > 0)
            {
			    if(dir.y > 0)
                {
				    float cx = (coord.x + 1) * cellSize;
				    float cy = (coord.y + 1) * cellSize;
				    dx = cx - currPos.x;
				    dy = cy - currPos.y;
			    }
                else
                {
				    float cx = (coord.x + 1) * cellSize;
				    float cy = coord.y * cellSize;
				    dx = cx - currPos.x;
				    dy = currPos.y - cy;
			    }
		    }
            else
            {
			    if(dir.y > 0)
                {
				    float cx = coord.x * cellSize;
				    float cy = (coord.y + 1) * cellSize;
				    dx = currPos.x - cx;
				    dy = cy - currPos.y;
			    }
                else
                {
				    float cx = coord.x * cellSize;
				    float cy = coord.y * cellSize;
				    dx = currPos.x - cx;
				    dy = currPos.y - cy;
			    }
		    }
		
		    float factorx = Math.Abs(dx / dir.x);
		    float factory = Math.Abs(dy / dir.y);
		
		    if(float.IsInfinity(factorx))
            {
			    factorx = 0.0f;
		    }

            if (float.IsInfinity(factory))
            {
			    factory = 0.0f;
		    }
		
		    if(factory == 0.0f)
            {
			    nextx = currPos.x + dir.x * factorx;
			    nexty = currPos.y + dir.y * factorx;
		    }
            else if(factorx == 0.0f)
            {
			    nextx = currPos.x + dir.x * factory;
			    nexty = currPos.y + dir.y * factory;
		    }
            else if(factorx < factory)
            {
			    nextx = currPos.x + dir.x * factorx;
			    nexty = currPos.y + dir.y * factorx;
		    }
            else
            {
			    nextx = currPos.x + dir.x * factory;
			    nexty = currPos.y + dir.y * factory;
		    }
		
		    Vector2D nextPos = new Vector2D(nextx, nexty);
		
		    Point newCell;
		    do
            {
			    nextPos.x += dir.x * FIX_DISTANCE;
			    nextPos.y += dir.y * FIX_DISTANCE;
			    newCell = posToCell(nextPos, cellSize);
		    }while(newCell.Equals(coord));
		
		    return nextPos;
	    }
	
	    public static Vector2D getNextPosBeforeNextCell(Vector2D currPos, Vector2D dir, float cellSize)
        {
		    Vector2D nextPos = getNextPos(currPos, dir, cellSize);
		    nextPos.x -= dir.x * FIX_DISTANCE * 2;
		    nextPos.y -= dir.y * FIX_DISTANCE * 2;
		    return nextPos;
	    }
	
	    /**
	     * Bresenham line algorithm
	     * @param start
	     * @param goal
	     * @return
	     */
	    public Point raycast(Vector2D startPos, Vector2D goalPos, float cellSize)
        {
		    Point startCell = posToCell(startPos, cellSize);
		    Point goalCell = posToCell(goalPos, cellSize);
		
		    // exception: start is obstacle. Now just return start
		    if(AStarCell.isObstacle(map.getCell(startCell.x, startCell.y)))
            {
			    return startCell;
		    }
		
		    if(startCell.Equals(goalCell))
            {
			    return startCell;
		    }
		
		    float diffx = goalPos.x - startPos.x;
		    float diffy = goalPos.y - startPos.y;
		    float distanceSquare = diffx * diffx + diffy * diffy;
		    float distance = (float)Math.Sqrt(distanceSquare);
		
		    Vector2D dir = new Vector2D(diffx / distance, diffy / distance);
		
		    Vector2D lastPassed = startPos;
		    Point hitPoint = new Point(startCell);
		    while(true)
            {
			    Vector2D nextPos = getNextPos(lastPassed, dir, cellSize);
			
			    Point newCell = posToCell(nextPos, cellSize);
			
			    float dis = (nextPos.x-startPos.x) * (nextPos.x-startPos.x) + (nextPos.y-startPos.y) * (nextPos.y-startPos.y);
			    if(dis >= distanceSquare)
                {
				    break;
			    }
			
			    if(AStarCell.isObstacle(map.getCell(newCell.x, newCell.y)))
                {
				    break;
			    }
                else
                {
				    hitPoint.x = newCell.x;
				    hitPoint.y = newCell.y;
				
				    if(newCell.Equals(goalCell))
                    {
					    break;
				    }
			    }
			
			    lastPassed = nextPos;
		    }
				
		    return hitPoint;
	    }
    }
}
