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
	 * @param cell1 the starting point
	 * @param cell2 the finishing point
	 * @return the line as a list of array elements
	 */
	public static ArrayList<Point> getCellsOnLine(Point cell1, Point cell2) {
		
		ArrayList<Point> line = new ArrayList<Point>();
		
		int dx = Math.abs(cell2.x - cell1.x);
		int dy = Math.abs(cell2.y - cell1.y);
		
		int sx = cell1.x < cell2.x ? 1 : -1; 
		int sy = cell1.y < cell2.y ? 1 : -1; 
		
		int err = dx-dy;
		int e2;
		int currentX = cell1.x;
		int currentY = cell1.y;
		
		while(true) {
			line.add(new Point(currentX, currentY));
			
			if(currentX == cell2.x && currentY == cell2.y) {
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
