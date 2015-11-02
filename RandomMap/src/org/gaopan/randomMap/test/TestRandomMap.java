package org.gaopan.randomMap.test;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gaopan.randomMap.Tile;
import org.gaopan.randomMap.TileMap;

public class TestRandomMap extends JFrame {    
	private static final int MAX_ROW = 5;
	private static final int MAX_COL = 5;
	private static final int MAX_TRY = 100;
	private static final int IMAGE_COUNT = 16;
	
	public static final String IMAGE_PATH = TestRandomMap.class.getResource("/").getPath() + "../../Resource/images" + File.separatorChar;
	
	private TileMap map;
	
	private JButton button1 = new JButton("1");
	private JButton button2 = new JButton("2");
	private JButton button3 = new JButton("3");
	private JButton buttonAll = new JButton("all");
	
	private JLabel statusBar = new JLabel("Cost:");
	
	private Map<Integer, ImageViewer> imageViewers = new HashMap<Integer, ImageViewer>();
	private Map<Integer, List<Image>> images = new HashMap<Integer, List<Image>>();
	
	public TestRandomMap() {
		init();
		
		JPanel p1 = new JPanel();
		p1.add(button1);
		p1.add(button2);
		p1.add(button3);
		p1.add(buttonAll);
		
		button1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				onButton1Clicked();
			}
		});
		
		button2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				onButton2Clicked();
			}
		});
		
		button3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				onButton3Clicked();
			}
		});
		
		buttonAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				onButtonAllClicked();
			}
		});
		
		JPanel p2 = new JPanel(new GridLayout(MAX_ROW, MAX_COL, 1, 1));
		for(int r = 0; r < MAX_ROW; ++r) {
			for(int c = 0; c < MAX_COL; ++c) {
				int imageViewerId = r * MAX_COL + c;
				ImageViewer imageViewer = imageViewers.get(imageViewerId);
				imageViewer.setBorder(BorderFactory.createTitledBorder("" + imageViewerId));
				p2.add(imageViewer);
			}
		}
		
		add(p1, BorderLayout.NORTH);
		add(p2, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);
	}
	
	private void init() {
		for(int i = 0; i < IMAGE_COUNT; ++i) {
			List<Image> imageList = new ArrayList<>();
			
			String fileName = IMAGE_PATH + i + ".gif";
			File file = new File(fileName);
			if(file.exists()) {
				Image image = new ImageIcon(fileName).getImage();
				imageList.add(image);
			} else {
				for(int j = 1; j <= 10; ++j) {
					fileName = IMAGE_PATH + i + "_" + j + ".gif";
					file = new File(fileName);
					if(!file.exists()) {
						break;
					}
					
					Image image = new ImageIcon(fileName).getImage();
					imageList.add(image);
				}
			}
			
			images.put(i, imageList);
		}
		
		for(int r = 0; r < MAX_ROW; ++r) {
			for(int c = 0; c < MAX_COL; ++c) {
				int imageViewerId = r * MAX_COL + c;
				ImageViewer imageViewer = new ImageViewer(images.get(0).get(0));
				imageViewers.put(imageViewerId, imageViewer);
			}
		}
	}
	
	private void onButton1Clicked() {
		long startTime = System.currentTimeMillis();
         map = new TileMap(MAX_ROW, MAX_COL, MAX_TRY);
         map.randomGenerateA();
         map.calcTileCrossType();
         long costTime = System.currentTimeMillis() - startTime;
 		statusBar.setText("Cost:" + costTime + "ms");
         draw();
	}
	
	private void onButton2Clicked() {
		long startTime = System.currentTimeMillis();
		map.randomGenerateB();
		map.calcTileCrossType();
		long costTime = System.currentTimeMillis() - startTime;
		statusBar.setText("Cost:" + costTime + "ms");
		draw();
	}
	
	private void onButton3Clicked() {
		long startTime = System.currentTimeMillis();
		map.randomGenerateC();
		map.calcTileCrossType();
		long costTime = System.currentTimeMillis() - startTime;
		statusBar.setText("Cost:" + costTime + "ms");
		draw();
	}
	
	private void onButtonAllClicked() {
		long startTime = System.currentTimeMillis();
		map = new TileMap(MAX_ROW, MAX_COL, MAX_TRY);
		map.randomGenerate1();
		map.calcTileCrossType();
		long costTime = System.currentTimeMillis() - startTime;
		statusBar.setText("Cost:" + costTime + "ms");
		draw();
	}
	
	private void draw()
    {
        for (int i = 0; i < map.getTilesCount(); ++i)
        {
            Tile tile = map.getTile(i);
            if (!tile.isValid())
            {
                imageViewers.get(i).setImage(images.get(0).get(0));
            }
            else
            {
            	int imageId = tile.getCrossMask();
            	List<Image> imageList = images.get(imageId);
            	int randIndex = new Random().nextInt(imageList.size());
                imageViewers.get(i).setImage(imageList.get(randIndex));
            }
        }
    }

	public static void main(String[] args) {
		TestRandomMap frame = new TestRandomMap();
		frame.setTitle("TestRandomMap");
		frame.setSize(800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
