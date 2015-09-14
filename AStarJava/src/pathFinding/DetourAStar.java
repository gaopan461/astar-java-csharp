package pathFinding;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import pathFinding.core.AStarCell;
import pathFinding.core.AStarMap;
import pathFinding.core.PathFinder2;
import pathFinding.utils.Vector2D;
import pathFinding.utils.Vector3D;

public class DetourAStar {
	private AStarMap astarMap;
	private PathFinder2 pathFinder;

	public DetourAStar(AStarMap astarMap) {
		super();
		this.astarMap = astarMap;
		this.pathFinder = new PathFinder2(astarMap);
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
	}

	public Vector3D raycast(Vector3D startPos, Vector3D endPos) {
		Vector2D startPos2D = new Vector2D(startPos.x, startPos.y);
		Vector2D endPos2D = new Vector2D(endPos.x, endPos.y);
		Vector2D hitPos = raycast(startPos2D, endPos2D);
		return getHeight(hitPos);
	}
	
	public Vector2D raycast(Vector2D startPos, Vector2D endPos) {
		Point startCell = PathFinder2.posToCell(startPos, astarMap.getCellSize());
		Point goalCell = PathFinder2.posToCell(endPos, astarMap.getCellSize());
		
		// exception: start is obstacle. Now just return start
		if(AStarCell.isObstacle(astarMap.getCell(startCell.x, startCell.y))) {
			return startPos;
		}
		
		if(startCell.equals(goalCell)) {
			return endPos;
		}
		
		Point hitCell = pathFinder.raycast(startPos, endPos, astarMap.getCellSize());
		Vector2D hitPos = PathFinder2.cellToPos(hitCell, astarMap.getCellSize());
		Vector2D dir = endPos.sub(startPos).normalize();
		hitPos = PathFinder2.getNextPosBeforeNextCell(hitPos, dir, astarMap.getCellSize());
		return hitPos;
	}

	public boolean isPosInBlock(Vector3D pos) {
		int cellX = (int)Math.floor(pos.x / astarMap.getCellSize());
		int cellY = (int)Math.floor(pos.y / astarMap.getCellSize());
		AStarCell cell = astarMap.getCell(cellX, cellY);
		return AStarCell.isObstacle(cell);
	}

}
