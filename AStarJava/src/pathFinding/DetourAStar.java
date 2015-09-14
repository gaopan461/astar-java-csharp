package pathFinding;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import pathFinding.core.AStarCell;
import pathFinding.core.AStarMap;
import pathFinding.core.PathFinder;
import pathFinding.utils.Vector2D;
import pathFinding.utils.Vector3D;

public class DetourAStar {
	private AStarMap astarMap;
	private PathFinder pathFinder;

	public DetourAStar(AStarMap astarMap) {
		super();
		this.astarMap = astarMap;
		this.pathFinder = new PathFinder(astarMap);
	}

	public Vector3D getHeight(Vector2D pos) {
		int cellX = (int)Math.floor(pos.x / astarMap.getCellSize());
		int cellY = (int)Math.floor(pos.y / astarMap.getCellSize());
		float height = astarMap.getHeight(cellX, cellY);
		return new Vector3D(pos.x, pos.y, height);
	}

	public Vector3D getHeightEx(Vector2D pos) {
		return getHeight(pos);
	}

	public List<Vector3D> findPaths(Vector3D startPos, Vector3D endPos) {
		Vector2D startPos2D = new Vector2D(startPos.x, startPos.y);
		Vector2D endPos2D = new Vector2D(endPos.x, endPos.y);
		
		List<Vector2D> path2D = pathFinder.findStraightPath(startPos2D, endPos2D, astarMap.getCellSize());
		if(path2D == null) {
			// 目的不可达
			return null;
		}
		
		List<Vector3D> path3D = new ArrayList<>();
		for(Vector2D p2D : path2D) {
			path3D.add(getHeight(p2D));
		}
		
		return path3D;
		
//		int startX = (int)Math.floor(startPos.x / astarMap.getCellSize());
//		int startY = (int)Math.floor(startPos.y / astarMap.getCellSize());
//		int endX = (int)Math.floor(endPos.x / astarMap.getCellSize());
//		int endY = (int)Math.floor(endPos.y / astarMap.getCellSize());
//		
//		List<Point> path2D = pathFinder.findStraightPath(new Point(startX, startY), new Point(endX, endY));
//		if(path2D == null) {
//			// 目的不可达
//			return null;
//		}
//		
//		List<Vector3D> path3D = new ArrayList<>();
//		for(Point p2D : path2D) {
//			float height = astarMap.getHeight(p2D.x, p2D.y);
//			float x = (p2D.x + 0.5f) * astarMap.getCellSize();
//			float y = (p2D.y + 0.5f) * astarMap.getCellSize();
//			path3D.add(new Vector3D(x, y, height));
//		}
		
//		if(path3D.size() >= 2) {
//			Vector3D first = path3D.get(0);
//			Vector3D second = path3D.get(1);
//			first.x = (first.x + second.x) / 2;
//			first.y = (first.y + second.y) / 2;
//		}
//		
//		if(path3D.size() >= 3) {
//			Vector3D last = path3D.get(path3D.size() - 1);
//			Vector3D lastSecond = path3D.get(path3D.size() - 2);
//			last.x = (last.x + lastSecond.x) / 2;
//			last.y = (last.y + lastSecond.y) / 2;
//		}
		
//		path3D.add(0, startPos);
//		path3D.add(endPos);
//		
//		return path3D;
	}

	public Vector3D raycast(Vector3D startPos, Vector3D endPos) {
		int startX = (int)Math.floor(startPos.x / astarMap.getCellSize());
		int startY = (int)Math.floor(startPos.y / astarMap.getCellSize());
		int endX = (int)Math.floor(endPos.x / astarMap.getCellSize());
		int endY = (int)Math.floor(endPos.y / astarMap.getCellSize());
		
		Point hitPoint = pathFinder.raycast(new Point(startX, startY), new Point(endX, endY));
		float x = (hitPoint.x + 0.5f) * astarMap.getCellSize();
		float y = (hitPoint.y + 0.5f) * astarMap.getCellSize();
		float height = astarMap.getHeight(hitPoint.x, hitPoint.y);
		return new Vector3D(x, y, height);
	}
	
	public Vector2D raycast(Vector2D startPos, Vector2D endPos) {
		int startX = (int)Math.floor(startPos.x / astarMap.getCellSize());
		int startY = (int)Math.floor(startPos.y / astarMap.getCellSize());
		int endX = (int)Math.floor(endPos.x / astarMap.getCellSize());
		int endY = (int)Math.floor(endPos.y / astarMap.getCellSize());
		
		Point hitPoint = pathFinder.raycast(new Point(startX, startY), new Point(endX, endY));
		float x = (hitPoint.x + 0.5f) * astarMap.getCellSize();
		float y = (hitPoint.y + 0.5f) * astarMap.getCellSize();
		return new Vector2D(x, y);
	}

	public boolean isPosInBlock(Vector3D pos) {
		int cellX = (int)Math.floor(pos.x / astarMap.getCellSize());
		int cellY = (int)Math.floor(pos.y / astarMap.getCellSize());
		AStarCell cell = astarMap.getCell(cellX, cellY);
		return AStarCell.isObstacle(cell);
	}

}
