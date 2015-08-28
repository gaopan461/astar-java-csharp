package pathFinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AStarData {
private static final String SPLIT_SUFFIX = ",";
	
	private int widthInCells;
	private int heightInCells;
	private float cellSize;
	private int[][] obstacleInfo;
	private float[][] heightInfo;
	
	public AStarData(File file) {
		String fileName = file.getName();
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			
			String[] headerInfo = br.readLine().split(SPLIT_SUFFIX);
			if(headerInfo.length != 3) {
				throw new RuntimeException("astar文件头数据不合法，文件:" + fileName);
			}
			
			widthInCells = Integer.parseInt(headerInfo[0]);
			heightInCells = Integer.parseInt(headerInfo[1]);
			cellSize = Float.parseFloat(headerInfo[2]);
			obstacleInfo = new int[heightInCells][widthInCells];
			heightInfo = new float[heightInCells][widthInCells];
			
			for(int h = 0; h < heightInCells; ++h) {
				String[] lineData = br.readLine().split(SPLIT_SUFFIX);
				if(lineData.length < widthInCells * 2) {
					throw new RuntimeException("astar寻路文件错误：读取文件错误。文件:" + fileName + ",行:" + (h+2));
				}
				
				for(int w = 0; w < widthInCells; ++w) {
					obstacleInfo[h][w] = Integer.parseInt(lineData[w*2]);
					heightInfo[h][w] = Float.parseFloat(lineData[w*2+1]);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("astar寻路文件错误：读取文件错误。文件:" + fileName);
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public int getWidthInCells() {
		return widthInCells;
	}

	public int getHeightInCells() {
		return heightInCells;
	}

	public float getCellSize() {
		return cellSize;
	}
	
	public int[][] getObstacleInfo() {
		return obstacleInfo;
	}

	public float[][] getHeightInfo() {
		return heightInfo;
	}

	public int getObstacle(int x, int y) {
		try {
			return obstacleInfo[y][x];
		} catch (Exception e) {
			return 1;
		}
	}
	
	public float getHeight(int x, int y) {
		try {
			return heightInfo[y][x];
		} catch (Exception e) {
			return 0f;
		}
	}
}
