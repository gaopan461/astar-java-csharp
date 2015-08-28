package pathFinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AStarData {
private static final String SPLIT_SUFFIX = ",";
	
	private int cellWidth;			// ��������(���)
	private int cellHeight;			// ��������(�߶�)
	private float cellSize;			// �����С(�����������Σ�ÿ�����������)
	private int[][] obstacleInfo;	// �����赲��Ϣ
	private float[][] heightInfo;	// ����߶���Ϣ
	
	public AStarData(File file) {
		String fileName = file.getName();
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			
			String[] headerInfo = br.readLine().split(SPLIT_SUFFIX);
			if(headerInfo.length != 3) {
				throw new RuntimeException("astar�ļ�ͷ���ݲ��Ϸ����ļ�:" + fileName);
			}
			
			cellWidth = Integer.parseInt(headerInfo[0]);
			cellHeight = Integer.parseInt(headerInfo[1]);
			cellSize = Float.parseFloat(headerInfo[2]);
			obstacleInfo = new int[cellHeight][cellWidth];
			heightInfo = new float[cellHeight][cellWidth];
			
			for(int h = 0; h < cellHeight; ++h) {
				String[] lineData = br.readLine().split(SPLIT_SUFFIX);
				if(lineData.length < cellWidth * 2) {
					throw new RuntimeException("astarѰ·�ļ����󣺶�ȡ�ļ������ļ�:" + fileName + ",��:" + (h+2));
				}
				
				for(int w = 0; w < cellWidth; ++w) {
					obstacleInfo[h][w] = Integer.parseInt(lineData[w*2]);
					heightInfo[h][w] = Float.parseFloat(lineData[w*2+1]);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("astarѰ·�ļ����󣺶�ȡ�ļ������ļ�:" + fileName);
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
