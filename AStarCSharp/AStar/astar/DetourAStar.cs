using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AStar.astar
{
    class DetourAStar
    {
        //private AStarMap astarMap;
        //private PathFinder pathFinder;

        //public DetourAStar(AStarMap astarMap) {
        //    this.astarMap = astarMap;
        //    this.pathFinder = new PathFinder(astarMap);
        //}

        //public Vector3D getHeight(Vector2D pos) {
        //    int cellX = (int)Math.Floor(pos.x / astarMap.getCellSize());
        //    int cellY = (int)Math.Floor(pos.y / astarMap.getCellSize());
        //    float height = astarMap.getHeight(cellX, cellY);
        //    return new Vector3D(pos.x, pos.y, height);
        //}

        //public Vector3D getHeightEx(Vector2D pos) {
        //    return getHeight(pos);
        //}

        //public List<Vector3D> findPaths(Vector3D startPos, Vector3D endPos) {
        //    int startX = (int)Math.Floor(startPos.x / astarMap.getCellSize());
        //    int startY = (int)Math.Floor(startPos.y / astarMap.getCellSize());
        //    int endX = (int)Math.Floor(endPos.x / astarMap.getCellSize());
        //    int endY = (int)Math.Floor(endPos.y / astarMap.getCellSize());
		
        //    List<Point> path2D = pathFinder.findStraightPath(new Point(startX, startY), new Point(endX, endY));
        //    List<Vector3D> path3D = new List<Vector3D>();
        //    foreach(Point p2D in path2D) {
        //        float height = astarMap.getHeight(p2D.x, p2D.y);
        //        float x = (p2D.x + 0.5f) * astarMap.getCellSize();
        //        float y = (p2D.y + 0.5f) * astarMap.getCellSize();
        //        path3D.Add(new Vector3D(x, y, height));
        //    }
		
        //    return path3D;
        //}

        //public Vector3D raycast(Vector3D startPos, Vector3D endPos) {
        //    int startX = (int)Math.Floor(startPos.x / astarMap.getCellSize());
        //    int startY = (int)Math.Floor(startPos.y / astarMap.getCellSize());
        //    int endX = (int)Math.Floor(endPos.x / astarMap.getCellSize());
        //    int endY = (int)Math.Floor(endPos.y / astarMap.getCellSize());
		
        //    Point hitPoint = pathFinder.raycast(new Point(startX, startY), new Point(endX, endY));
        //    float x = (hitPoint.x + 0.5f) * astarMap.getCellSize();
        //    float y = (hitPoint.y + 0.5f) * astarMap.getCellSize();
        //    float height = astarMap.getHeight(hitPoint.x, hitPoint.y);
        //    return new Vector3D(x, y, height);
        //}
	
        //public Vector2D raycast(Vector2D startPos, Vector2D endPos) {
        //    int startX = (int)Math.Floor(startPos.x / astarMap.getCellSize());
        //    int startY = (int)Math.Floor(startPos.y / astarMap.getCellSize());
        //    int endX = (int)Math.Floor(endPos.x / astarMap.getCellSize());
        //    int endY = (int)Math.Floor(endPos.y / astarMap.getCellSize());
		
        //    Point hitPoint = pathFinder.raycast(new Point(startX, startY), new Point(endX, endY));
        //    float x = (hitPoint.x + 0.5f) * astarMap.getCellSize();
        //    float y = (hitPoint.y + 0.5f) * astarMap.getCellSize();
        //    return new Vector2D(x, y);
        //}

        //public bool isPosInBlock(Vector3D pos) {
        //    int cellX = (int)Math.Floor(pos.x / astarMap.getCellSize());
        //    int cellY = (int)Math.Floor(pos.y / astarMap.getCellSize());
        //    AStarCell cell = astarMap.getCell(cellX, cellY);
        //    return AStarCell.isObstacle(cell);
        //}
    }
}
