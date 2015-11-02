package org.gaopan.randomMap.test;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImageViewer extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Image image;
	private int xCoordinate;
	private int yCoordinate;
	private boolean stretched = true;
	
	public ImageViewer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ImageViewer(Image image) {
		super();
		this.image = image;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
		this.repaint();
	}

	public int getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
		this.repaint();
	}

	public int getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
		this.repaint();
	}

	public boolean isStretched() {
		return stretched;
	}

	public void setStretched(boolean stretched) {
		this.stretched = stretched;
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(image != null) {
			if(stretched) {
				g.drawImage(image, xCoordinate, yCoordinate, getWidth(), getHeight(), this);
			} else {
				g.drawImage(image, xCoordinate, yCoordinate, this);
			}
		}
	}
	
}
