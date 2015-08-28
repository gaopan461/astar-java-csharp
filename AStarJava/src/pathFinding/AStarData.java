package pathFinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AStarData {
private static final String SPLIT_SUFFIX = ",";
	
	private int cellWidth;			// 网格列数(宽度)
	private int cellHeight;			// 网格行数(高度)
	private float cellSize;			// 网格大小(网格是正方形，每个网格多少米)
	private int[][] obstacleInfo;	// 网格阻挡信息
	private float[][] heightInfo;	// 网格高度信息
	
	public AStarData(File file) {
		String fileName = file.getName();
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			
			String[] headerInfo = br.readLine().split(SPLIT_SUFFIX);
			if(headerInfo.length != 3) {
				throw new RuntimeException("astar文件头数据不合法，文件:" + fileName);
			}
			
			cellWidth = Integer.parseInt(headerInfo[0]);
			cellHeight = Integer.parseInt(headerInfo[1]);
			cellSize = Float.parseFloat(headerInfo[2]);
			obstacleInfo = new int[cellHeight][cellWidth];
			heightInfo = new float[cellHeight][cellWidth];
			
			for(int h = 0; h < cellHeight; ++h) {
				String[] lineData = br.readLine().split(SPLIT_SUFFIX);
				if(lineData.length < cellWidth * 2) {
					throw new RuntimeException("astar寻路文件错误：读取文件错误。文件:" + fileName + ",行:" + (h+2));
				}
				
				for(int w = 0; w < cellWidth; ++w) {
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

	public int getCellWidth() {
		return cellWidth;
	}

	public int getCellHeight() {
		return cellHeight;
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
