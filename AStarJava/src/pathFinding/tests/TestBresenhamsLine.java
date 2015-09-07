package pathFinding.tests;

import java.awt.Point;
import java.util.ArrayList;

import pathFinding.AStarCellMgr;
import pathFinding.AStarSingleTileMap;
import pathFinding.bresenhamsLine.Bresenham;
import pathFinding.core.AStarMap;
import pathFinding.graphics.PrintMap;
import pathFinding.utils.Logger;
import pathFinding.utils.StopWatch;

public class TestBresenhamsLine {	
	private static final String TILE_ID = "normal2";
	
	public static void main(String[] args) {
		Point a = new Point(22, 23);
		Point b = new Point(0, 0);
		
		Logger log = new Logger();
		StopWatch s = new StopWatch();
		
		log.addToLog("Initializing map...");
		AStarMap map = new AStarSingleTileMap(TILE_ID, new AStarCellMgr());
		
		log.addToLog("Generating Bresenham's Line from "+a.x+","+a.y+" to "+b.x+","+b.y+"...");
		s.start();
		ArrayList<Point> line = Bresenham.getCellsOnLine(a, b);
		s.stop();
		log.addToLog("Generation took " + s.getElapsedTimeUSecs() + " us");
		
		String str = "";
		for (Point point : line) {
			str = str+"("+point.x+","+point.y+") ";
		}
		log.addToLog("Line is:" + str);
		
		log.addToLog("Writing line to map...");
		log.addToLog("Printing map...");
		new PrintMap(map, line);
		
	}

}
