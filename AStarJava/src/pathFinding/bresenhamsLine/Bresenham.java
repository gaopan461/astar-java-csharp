package pathFinding.bresenhamsLine;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Implementation of the Bresenham line algorithm.
 * @author fragkakis
 *
 */
public class Bresenham {
	
	/**
	 * Returns the list of array elements that comprise the line. 
	 * @param start the starting point
	 * @param goal the finishing point
	 * @return the line as a list of array elements
	 */
	public static ArrayList<Point> getCellsOnLine(Point start, Point goal) {
		
		ArrayList<Point> line = new ArrayList<Point>();
		
		int dx = Math.abs(goal.x - start.x);
		int dy = Math.abs(goal.y - start.y);
		
		int sx = start.x < goal.x ? 1 : -1; 
		int sy = start.y < goal.y ? 1 : -1; 
		
		int err = dx-dy;
		int e2;
		int currentX = start.x;
		int currentY = start.y;
		
		while(true) {
			line.add(new Point(currentX, currentY));
			
			if(currentX == goal.x && currentY == goal.y) {
				break;
			}
			
			e2 = 2*err;
			if(e2 > -1 * dy) {
				err = err - dy;
				currentX = currentX + sx;
			}
			
			if(e2 < dx) {
				err = err + dx;
				currentY = currentY + sy;
			}
		}
				
		return line;
	}
	
}
