package pathFinding.graphics;

import java.awt.Point;
import java.util.ArrayList;

import pathFinding.core.AStarCell;
import pathFinding.core.AStarMap;

public class PrintMap {
	public PrintMap(AStarMap map, ArrayList<Point> shortestPath) {
		AStarCell cell;
		for(int y = 0; y < map.getHeightInCells(); y++) {
			if(y == 0) {
				for (int i = 0; i <= map.getHeightInCells(); i++)
					System.out.print("-");
				System.out.println();	
			}
			System.out.print("|");

			for(int x = 0; x < map.getWidthInCells(); x++) {
				cell = map.getCell(x, y);
				if(AStarCell.isObstacle(cell)) {
					System.out.print("X");
				} else if(isStart(shortestPath, cell.getPoint())) {
					System.out.print("s");
				} else if(isGoal(shortestPath, cell.getPoint())) {
					System.out.print("g");
				} else if (shortestPath.contains(new Point(cell.getX(), cell.getY()))) {
					System.out.print("?");
				} else {
					System.out.print(" ");
				}
				if(y == map.getHeightInCells())
					System.out.print("_");
			}

			System.out.print("|");
			System.out.println();
		}
		for (int i = 0; i <= map.getHeightInCells(); i++)
			System.out.print("-");
	}
	
	private static boolean isStart(ArrayList<Point> shortestPath, Point point) {
		if(shortestPath.isEmpty()) {
			return false;
		}
		
		return shortestPath.get(0).equals(point);
	}
	
	private static boolean isGoal(ArrayList<Point> shortestPath, Point point) {
		if(shortestPath.isEmpty()) {
			return false;
		}
		
		return shortestPath.get(shortestPath.size() - 1).equals(point);
	}
}
