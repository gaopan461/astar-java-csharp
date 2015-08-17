package pathFinding.graphics;

import java.awt.Point;
import java.util.ArrayList;

import pathFinding.AStarCell;
import pathFinding.AStarMap;

public class PrintMap {
	public PrintMap(AStarMap map, ArrayList<Point> shortestPath) {
		AStarCell cell;
		for(int y = 0; y < map.getMapHeight(); y++) {
			if(y == 0) {
				for (int i = 0; i <= map.getMapHeight(); i++)
					System.out.print("-");
				System.out.println();	
			}
			System.out.print("|");

			for(int x = 0; x < map.getMapWith(); x++) {
				cell = map.getCell(x, y);
				if(cell.isObstacle()) {
					System.out.print("X");
				} else if(cell.isStart()) {
					System.out.print("s");
				} else if(cell.isGoal()) {
					System.out.print("g");
				} else if (shortestPath.contains(new Point(cell.getX(), cell.getY()))) {
					System.out.print("?");
				} else {
					System.out.print(" ");
				}
				if(y == map.getMapHeight())
					System.out.print("_");
			}

			System.out.print("|");
			System.out.println();
		}
		for (int i = 0; i <= map.getMapHeight(); i++)
			System.out.print("-");
	}
}
