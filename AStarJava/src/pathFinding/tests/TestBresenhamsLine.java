package pathFinding.tests;

import java.awt.Point;
import java.util.ArrayList;

import pathFinding.AStarMap;
import pathFinding.bresenhamsLine.BresenhamsLine;
import pathFinding.graphics.PrintMap;
import pathFinding.utils.Logger;
import pathFinding.utils.StopWatch;

public class TestBresenhamsLine {
	
	private static int mapWidth = 50;
	private static int mapHeight = 50;
	
	public static void main(String[] args) {
		Point a = new Point(49, 49);
		Point b = new Point(0, 0);
		
		Logger log = new Logger();
		StopWatch s = new StopWatch();
		
		log.addToLog("Initializing "+mapWidth+"x"+mapHeight+" map...");
		AStarMap map = new AStarMap(mapWidth, mapHeight);
		
		log.addToLog("Generating Bresenham's Line from "+a.x+","+a.y+" to "+b.x+","+b.y+"...");
		s.start();
		ArrayList<Point> line = BresenhamsLine.getCellsOnLine(a, b);
		s.stop();
		log.addToLog("Generation took " + s.getElapsedTime() + " ms");
		
		String str = "";
		for (Point point : line) {
			str = str+"("+point.x+","+point.y+") ";
		}
		log.addToLog("Line is:" + str);
		
		log.addToLog("Writing line to map...");
		for(Point point : line) {
			map.setObstacle(point.x, point.y, true);
		}
		log.addToLog("Printing map...");
		new PrintMap(map, new ArrayList<Point>());
		
	}

}
