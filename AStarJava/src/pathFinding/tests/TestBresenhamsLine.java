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
	
	private static int mapWidth = 50;
	private static int mapHeight = 50;
	private static int obstacleMap[][] = new int[mapHeight][mapWidth];
	
	private static final String TILE_ID = "normal2";
	
	static {
		for(int h = 0; h < mapHeight; ++h) {
			for(int w = 0; w < mapWidth; ++w) {
				obstacleMap[h][w] = 0;
			}
		}
	}
	
	public static void main(String[] args) {
		Point a = new Point(22, 23);
		Point b = new Point(0, 0);
		
		Logger log = new Logger();
		StopWatch s = new StopWatch();
		
		log.addToLog("Initializing "+mapWidth+"x"+mapHeight+" map...");
		AStarMap map = new AStarSingleTileMap(TILE_ID, new AStarCellMgr());
		
		log.addToLog("Generating Bresenham's Line from "+a.x+","+a.y+" to "+b.x+","+b.y+"...");
		s.start();
		ArrayList<Point> line = Bresenham.getCellsOnLine(a, b);
		s.stop();
		log.addToLog("Generation took " + s.getElapsedTime() + " ms");
		
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
