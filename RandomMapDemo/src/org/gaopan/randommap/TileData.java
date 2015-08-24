package org.gaopan.randommap;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;

public class TileData {
	public static final String IMAGE_PATH = "images" + File.separatorChar;
	public static final String ASTAR_PATH = "astar" + File.separatorChar;
	
	public static final String IMAGE_SUFFIX = ".gif";
	public static final String MAP_SUFFIX = ".txt";
	
	private String fileName;
	
	private ImageIcon imageIcon;
	
	private int[][] astar;
	private int astarWidth;
	private int astarHeight;
	
	public TileData(String fileName) {
		this.fileName = fileName;
		loadImage();
		loadAstar();
	}
	
	private void loadImage() {
		String actualName = IMAGE_PATH + fileName + IMAGE_SUFFIX;
		File file = new File(actualName);
		if(!file.exists()) {
			throw new RuntimeException("file not exist:" + actualName);
		}
		
		imageIcon = new ImageIcon(actualName);
	}
	
	private void loadAstar() {
		String actualName = ASTAR_PATH + fileName + MAP_SUFFIX;
		File file = new File(actualName);
		if(!file.exists()) {
			throw new RuntimeException("file not exist:" + actualName);
		}
		
        try {
            FileReader freader = new FileReader(file);
            BufferedReader breader = new BufferedReader(freader);
            String line = breader.readLine();
            if(line == null) {
            	throw new RuntimeException("file empty:" + actualName);
            }
            
            String[] values = line.split(",");
            if(values.length < 2) {
            	throw new RuntimeException("file invalid:" + actualName);
            }
            
            int width = Integer.parseInt(values[0]);
            int height = Integer.parseInt(values[1]);
            if(width <= 0 || height <= 0) {
            	throw new RuntimeException("file invalid:" + actualName);
            }
            
            int[][] astar = new int[height][width];
            for(int h = 0; h < height; ++h) {
            	line = breader.readLine();
            	if(line == null) {
            		throw new RuntimeException("file invalid:" + actualName);
            	}
            	
            	values = line.split(",");
                if(values.length < width) {
                	throw new RuntimeException("file invalid:" + actualName);
                }
                
                for(int w = 0; w < width; ++w) {
                	int obstacle = Integer.parseInt(values[w]);
                	astar[h][w] = (obstacle == 0) ? 0 : 1;
                }
            }
            
            this.astar = astar;
            this.astarWidth = width;
            this.astarHeight = height;
           
            breader.close();
            freader.close();
        } catch (FileNotFoundException ex) {
            // TODO Auto-generated catch block
        	ex.printStackTrace();
        } catch (IOException ex) {
            // TODO Auto-generated catch block
        	ex.printStackTrace();
        }
	}
	
	public String getFileName() {
		return fileName;
	}

	public Image getImage() {
		return imageIcon.getImage();
	}
	
	public int getImageWidth() {
		return imageIcon.getIconWidth();
	}
	
	public int getImageHeight() {
		return imageIcon.getIconHeight();
	}

	public int[][] getAstar() {
		return astar;
	}

	public int getAstarWidth() {
		return astarWidth;
	}

	public int getAstarHeight() {
		return astarHeight;
	}
	
}
