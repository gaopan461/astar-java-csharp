package org.gaopan.randomMap;

import java.util.ArrayList;

public class Tile {
	private int crossMask = 0;
	private ArrayList<Integer> next = new ArrayList<Integer>();
	
	public int getCrossMask() {
		return crossMask;
	}
	
	public void setCrossMask(int crossMask) {
		this.crossMask = crossMask;
	}
	
	public ArrayList<Integer> getCrossed() {
		return next;
	}
	
	public boolean isValid() {
		return next.size() > 0;
	}
}
