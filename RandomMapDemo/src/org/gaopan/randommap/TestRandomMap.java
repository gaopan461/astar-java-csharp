package org.gaopan.randommap;

import java.awt.FileDialog;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import xtile.Tile;
import xtile.TileMap;

public class TestRandomMap extends JFrame {    
	private static final int MAX_ROW = 5;
	private static final int MAX_COL = 5;
	private static final int MAX_TRY = 100;
	private static final int IMAGE_COUNT = 16;
	
	private int tileImageWidth;
	private int tileImageHeight;
	private int tileAstarWidth;
	private int tileAstarHeight;
	
	private TileMap tileMap;
	private int[][] obstacleMap;
	
	private JButton buttonRandom = new JButton("random");
	private JLabel statusBar = new JLabel("Cost:");
	private MapPanel mapPanel;
	
	private JButton buttonSaveAstar = new JButton("saveAstar");
	private FileDialog fdSave;
	
	private Map<Integer, List<TileData>> id2Tiles = new HashMap<Integer, List<TileData>>();
	
	public TestRandomMap() {
		init();
		
		fdSave = new FileDialog(this, "Save", FileDialog.SAVE);
		fdSave.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				fdSave.setVisible(false);
				super.windowClosing(arg0);
			}
		});
		
		JPanel p1 = new JPanel();
		p1.add(buttonRandom);
		p1.add(buttonSaveAstar);
		
		buttonRandom.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				onButtonAllClicked();
			}
		});
		
		buttonSaveAstar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fdSave.setVisible(true);
				
				String fileName = fdSave.getFile();
				if(fileName != null)
                {
					TestRandomMap.this.saveData();
                }
			}
		});
		
		buttonSaveAstar.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
			}
		});
		
		mapPanel = new MapPanel(this, tileImageWidth / tileAstarWidth);
		onButtonAllClicked();
		
		setLayout(null);
		add(p1);
		add(mapPanel);
		add(statusBar);
		
		p1.setBounds(0, 0, MAX_COL * tileImageWidth, 50);
		mapPanel.setBounds(0, 50, MAX_COL * tileImageWidth, MAX_ROW * tileImageHeight);
		statusBar.setBounds(0, 50 + MAX_ROW * tileImageHeight, MAX_COL * tileImageWidth, 50);
	}
	
	private void init() {
		// 加载地块图片和寻路数据
		for(int i = 0; i < IMAGE_COUNT; ++i) {
			List<TileData> tileList = new ArrayList<>();
			
			String fileName = "" + i;
			File file = new File(TileData.IMAGE_PATH + fileName + TileData.IMAGE_SUFFIX);
			if(file.exists()) {
				TileData tileData = new TileData(fileName);
				tileList.add(tileData);
			} else {
				for(int j = 1; j <= 10; ++j) {
					fileName = i + "_" + j;
					file = new File(TileData.IMAGE_PATH + fileName + TileData.IMAGE_SUFFIX);
					if(!file.exists()) {
						break;
					}
					
					TileData tileData = new TileData(fileName);
					tileList.add(tileData);
				}
			}
			
			id2Tiles.put(i, tileList);
		}
		
		TileData tile0 = id2Tiles.get(0).get(0);
		tileImageWidth = tile0.getImageWidth();
		tileImageHeight = tile0.getImageHeight();
		tileAstarWidth = tile0.getAstarWidth();
		tileAstarHeight = tile0.getAstarHeight();
		
		if(tileImageWidth * tileAstarHeight != tileImageHeight * tileAstarWidth) {
			throw new RuntimeException("image and astar data not match");
		}
		
		// 检测所有地块是否是相同尺寸
		for(List<TileData> tileList : id2Tiles.values()) {
			for(TileData tile : tileList) {
				if(tile.getImageWidth() != tileImageWidth
						|| tile.getImageHeight() != tileImageHeight
						|| tile.getAstarWidth() != tileAstarWidth
						|| tile.getAstarHeight() != tileAstarHeight) {
					throw new RuntimeException("file size not equal:" + tile.getFileName());
				}
			}
		}
	}
	
	private void onButtonAllClicked() {
		randomMap();
		updateMap();
	}
	
	private void randomMap() {
		long startTime = System.currentTimeMillis();
		tileMap = new TileMap(MAX_ROW, MAX_COL, MAX_TRY);
		tileMap.randomGenerate1();
		tileMap.calcTileCrossType();
		long costTime = System.currentTimeMillis() - startTime;
		statusBar.setText("Cost:" + costTime + "ms");
	}
	
	private void updateMap()
    {
		int[][] astar = new int[MAX_ROW * tileAstarHeight][MAX_COL * tileAstarWidth];
		Image[] images = new Image[MAX_ROW * MAX_COL];
		
		for(int r = 0; r < MAX_ROW; ++r) {
			for(int c = 0; c < MAX_COL; ++c) {
				Tile tile = tileMap.getTile(r, c);
				int imageId = tile.getCrossMask();
				List<TileData> tileList = id2Tiles.get(imageId);
				TileData tileData = null;
				if(tileList.size() == 1) {
					tileData = tileList.get(0);
	        	} else {
		        	int randIndex = new Random().nextInt(tileList.size());
		        	tileData = tileList.get(randIndex);
	        	}
				
				images[r * MAX_COL + c] = tileData.getImage();
				int[][] tileAstar = tileData.getAstar();
				for(int h = 0; h < tileAstarHeight; ++h) {
					for(int w = 0; w < tileAstarWidth; ++w) {
						int astarx = c * tileAstarWidth + w;
						int astary = r * tileAstarHeight + h;
						astar[astary][astarx] = tileAstar[h][w];
					}
				}
			}
		}
        
        mapPanel.setImages(images, MAX_COL, MAX_ROW);
        mapPanel.setAstar(astar, MAX_COL * tileAstarWidth, MAX_ROW * tileAstarHeight);
        
        this.obstacleMap = astar;
    }

	public static void main(String[] args) {
		TestRandomMap frame = new TestRandomMap();
		frame.setTitle("TestRandomMap");
		frame.setSize(MAX_COL * frame.tileImageWidth + 50, MAX_ROW * frame.tileImageHeight + 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(false);
	}

	public JLabel getStatusBar() {
		return statusBar;
	}
	
	private void saveData() {
		File file = new File(fdSave.getDirectory(),fdSave.getFile());
        try {
        	FileWriter fwriter = new FileWriter(file);
        	BufferedWriter bwriter = new BufferedWriter(fwriter);
            
        	int[][] map = this.obstacleMap;
        	int width = MAX_COL * tileAstarWidth;
        	int height = MAX_ROW * tileAstarHeight;
        	
        	bwriter.write(width + "," + height);
        	bwriter.newLine();
        	
        	for(int h = 0; h < height; ++h) {
        		for(int w = 0; w < width; ++w) {
        			bwriter.write(map[h][w] + ",");
        		}
        		bwriter.newLine();
        	}
        	
            bwriter.close();  
            fwriter.close();  
        } catch (IOException ex) {  
            // TODO Auto-generated catch block  
            ex.printStackTrace();  
        } 
	}

}
