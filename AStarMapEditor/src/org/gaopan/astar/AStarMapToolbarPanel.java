package org.gaopan.astar;

import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AStarMapToolbarPanel extends JPanel {
	private static final int WHITE_PIXEL_VALUE = 255 * 3 - 10;
	private static final float DEFAULT_CELL_SIZE = 0f;
	private static final float DEFAULT_HEIGHT = 0f;
	
	private AStarMapEditor editorFrame;
	
	private JLabel jlbCellWidth = new JLabel("Width:");
	private JTextField jtfCellWidth = new JTextField(5);
	private JLabel jlbCellHeight = new JLabel("Height:");
	private JTextField jtfCellHeight = new JTextField(5);

	private JButton jbtFillAll = new JButton("FillAll");
	private JButton jbtClearAll = new JButton("ClearAll");
	private JButton jbtSave = new JButton("Save");
	private JButton jbtOpen = new JButton("Open");
	
	private FileDialog fdSave = new FileDialog(editorFrame, "Save", FileDialog.SAVE);
	private FileDialog fdOpen = new FileDialog(editorFrame, "Open", FileDialog.LOAD);
	
	public AStarMapToolbarPanel(AStarMapEditor editorFrame) {
		this.editorFrame = editorFrame;
		
		JPanel p1 = new JPanel(new GridLayout(2, 2, 5, 5));
		p1.add(jlbCellWidth);
		p1.add(jtfCellWidth);
		p1.add(jlbCellHeight);
		p1.add(jtfCellHeight);
		
		JPanel p2 = new JPanel(new GridLayout(2, 2, 5, 5));
		p2.add(jbtFillAll);
		p2.add(jbtClearAll);
		p2.add(jbtSave);
		p2.add(jbtOpen);
		
		add(p1);
		add(p2);
		
		jtfCellWidth.setText("" + editorFrame.getEditorPanel().getCellWidth());
		jtfCellHeight.setText("" + editorFrame.getEditorPanel().getCellHeight());
		
		jtfCellWidth.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				super.keyTyped(e);
				if(e.getKeyChar() == '\n') {
					int cellWidth = Integer.parseInt(jtfCellWidth.getText());
					if(cellWidth > 0) {
						editorFrame.setCellWidth(cellWidth);
					}
				}
			}
		});
		
		jtfCellHeight.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				super.keyTyped(e);
				if(e.getKeyChar() == '\n') {
					int cellHeight = Integer.parseInt(jtfCellHeight.getText());
					if(cellHeight > 0) {
						editorFrame.setCellHeight(cellHeight);
					}
				}
			}
		});
		
		jbtFillAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				editorFrame.getEditorPanel().fillAll();
			}
		});
		
		jbtClearAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				editorFrame.getEditorPanel().clearAll();
			}
		});
		
		jbtSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fdSave.setVisible(true);
				
				String fileName = fdSave.getFile();
				if(fileName != null)
                {
					AStarMapToolbarPanel.this.saveData();
                }
			}
		});
		
		jbtOpen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fdOpen.setVisible(true);
				
				String fileName = fdOpen.getFile();
				if(fileName != null)
                {
                    if(fileName.toLowerCase().endsWith(".jpg")
                    		|| fileName.toLowerCase().endsWith(".png")
                    		|| fileName.toLowerCase().endsWith(".gif")) {
                    	AStarMapToolbarPanel.this.loadImage();
                    } else {
                    	AStarMapToolbarPanel.this.loadData();
                    }
                }
			}
		});
		
		fdSave.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				fdSave.setVisible(false);
				super.windowClosing(arg0);
			}
		});
		
		fdOpen.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				fdOpen.setVisible(false);
				super.windowClosing(e);
			}
		});
	}
	
	public void setCellWidth(int cellWidth) {
		jtfCellWidth.setText("" + cellWidth);
	}
	
	public void setCellHeight(int cellHeight) {
		jtfCellHeight.setText("" + cellHeight);
	}
	
	private void saveData() {
		File file = new File(fdSave.getDirectory(),fdSave.getFile());
        try {
        	FileWriter fwriter = new FileWriter(file);
        	BufferedWriter bwriter = new BufferedWriter(fwriter);
            
        	int[][] map = editorFrame.getEditorPanel().getMap();
        	int width = editorFrame.getEditorPanel().getCellWidth();
        	int height = editorFrame.getEditorPanel().getCellHeight();
        	
        	bwriter.write(width + "," + height + "," + DEFAULT_CELL_SIZE);
        	bwriter.newLine();
        	
        	for(int h = 0; h < height; ++h) {
        		for(int w = 0; w < width; ++w) {
        			bwriter.write(map[h][w] + "," + DEFAULT_HEIGHT + ",");
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
	
	private void loadData() {
		File file = new File(fdOpen.getDirectory(), fdOpen.getFile());
        try {
            FileReader freader = new FileReader(file);
            BufferedReader breader = new BufferedReader(freader);
            String line = breader.readLine();
            if(line == null) {
            	throw new RuntimeException("file empty");
            }
            
            String[] values = line.split(",");
            if(values.length < 3) {
            	throw new RuntimeException("file invalid");
            }
            
            int width = Integer.parseInt(values[0]);
            int height = Integer.parseInt(values[1]);
            // ingore cell size
            if(width <= 0 || height <= 0) {
            	throw new RuntimeException("file invalid");
            }
            
            int[][] map = new int[height][width];
            for(int h = 0; h < height; ++h) {
            	line = breader.readLine();
            	if(line == null) {
            		throw new RuntimeException("file invalid");
            	}
            	
            	values = line.split(",");
                if(values.length < width * 2) {
                	throw new RuntimeException("file invalid");
                }
                
                for(int w = 0; w < width; ++w) {
                	int obstacle = Integer.parseInt(values[w*2]);
                	// ingore height info
                	map[h][w] = (obstacle == 0) ? 0 : 1;
                }
            }
            
            editorFrame.loadMap(map, width, height);
           
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
	
	private void loadImage() {
		File file = new File(fdOpen.getDirectory(), fdOpen.getFile());
		BufferedImage bImage = null;
		try {
			bImage = ImageIO.read(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int pixelWidth = bImage.getWidth();
		int pixelHeight = bImage.getHeight();
		if(bImage.getMinX() != 0 || bImage.getMinY() != 0) {
			throw new RuntimeException("file invalid");
		}
		
		int cellWidth = editorFrame.getEditorPanel().getCellWidth();
		int cellHeight = editorFrame.getEditorPanel().getCellHeight();
		
		float cellPixelWidth = (float)pixelWidth / cellWidth;
		float cellPixelHeight = (float)pixelHeight / cellHeight;
		float cellPixelSize = Math.min(cellPixelWidth, cellPixelHeight);
		
		int[][] map = new int[cellHeight][cellWidth];
		for(int h = 0; h < cellHeight; ++h) {
			for(int w = 0; w < cellWidth; ++w) {
				int obstacle = convertPixelToObstacle(bImage, w, h, cellPixelSize);
				map[h][w] = obstacle;
			}
		}
		
		editorFrame.loadMap(map, cellWidth, cellHeight);
	}
	
	private static int convertPixelToObstacle(BufferedImage bImage, int width, int height, float cellPixelSize) {
		int startx = (int)(width * cellPixelSize);
		int starty = (int)(height * cellPixelSize);
		int endx = (int)((width + 1) * cellPixelSize);
		int endy = (int)((height + 1) * cellPixelSize);
		
		int pixelSumValue = 0;
		for(int h = starty; h < endy; ++h) {
			for(int w = startx; w < endx; ++w) {
				int pixel = bImage.getRGB(w, h);
				int pixelValue = ((pixel & 0xff0000) >> 16) + ((pixel & 0xff00) >> 8) + (pixel & 0xff);
				pixelSumValue += pixelValue;
			}
		}
		
		int pixelNum = (endy - starty) * (endx - startx);
		int pixelAvgValue = pixelSumValue / pixelNum;
		if(pixelAvgValue < WHITE_PIXEL_VALUE) {
			return AStarMapEditor.OBSTACLE_VALUE;
		} else {
			return AStarMapEditor.PASS_VALUE;
		}
	}

}
