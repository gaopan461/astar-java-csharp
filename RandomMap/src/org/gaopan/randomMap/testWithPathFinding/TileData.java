package org.gaopan.randomMap.testWithPathFinding;

import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;

public class TileData {
	public static final String IMAGE_PATH = TileData.class.getResource("/").getPath() + "../../Resource/images" + File.separatorChar;
	
	public static final String IMAGE_SUFFIX = ".gif";
	
	private String fileName;
	
	private ImageIcon imageIcon;
	
	public TileData(String fileName) {
		this.fileName = fileName;
		loadImage();
	}
	
	private void loadImage() {
		String actualName = IMAGE_PATH + fileName + IMAGE_SUFFIX;
		File file = new File(actualName);
		if(!file.exists()) {
			throw new RuntimeException("file not exist:" + actualName);
		}
		
		imageIcon = new ImageIcon(actualName);
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
	
}
