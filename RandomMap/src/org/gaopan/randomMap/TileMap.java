package org.gaopan.randomMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class TileMap {
	private int maxRow = 10;
    private int maxCol = 6;
    private int maxTry = 100;
    
    private Tile[] tiles;
    
    public TileMap(int maxRow, int maxCol, int maxTry) {
    	this.maxRow = maxRow;
    	this.maxCol = maxCol;
    	this.maxTry = maxTry;
    	init();
    }
    
    private void init() {
    	tiles = new Tile[maxRow * maxCol];
        for (int i = 0; i < maxRow * maxCol; ++i) {
            Tile tile = new Tile();
            tiles[i] = tile;
        }
    }
    
    public int getTilesCount() {
        return tiles.length;
    }
    
    public Tile getTile(int index) {
        if (index < 0 || index >= maxRow * maxCol) {
            return null;
        }
        return tiles[index];
    }
    
    public Tile getTile(int i, int j) {
        int index = i * maxCol + j;
        return getTile(index);
    }
    
    public int getIndexWithIJ(int i, int j) {
        return i * maxCol + j;
    }
    
    public int getTileRow(int index) {
        return index / maxCol;
    }
    
    public int getTileCol(int index) {
        return index % maxCol;
    }
    
    public int getUpTileIndex(int index) {
        int row = getTileRow(index);
        if (row == 0) {
            return -1;
        }
        return index - maxCol;
    }
    
    public int getDownTileIndex(int index) {
        int row = getTileRow(index);
        if (row == maxRow - 1) {
            return -1;
        }
        return index + maxCol;
    }
    
    public int getLeftTileIndex(int index) {
        int col = getTileCol(index);
        if (col == 0) {
            return -1;
        }
        return index - 1;
    }
    
    public int getRightTileIndex(int index) {
        int col = getTileCol(index);
        if (col == maxCol - 1) {
            return -1;
        }
        return index + 1;
    }

    public void randomGenerateA() {
        for (int i = 0; i < maxRow * maxCol; ++i) {
            tiles[i].setCrossMask(0);
            tiles[i].getCrossed().clear();
        }

        // @xl 第一步先随机拆墙，构建基础路径 //
        for (int i = 1; i < maxRow - 1; ++i) {
            for (int j = 1; j < maxCol - 1; ++j) {
                Tile tile = getTile(i, j);
                Tile crossTile = null;
                int index = getIndexWithIJ(i, j);
                int crossIndex = -1;
                int randDir = new Random().nextInt(4) + 1;
                switch(randDir) {
                case 1:
                	crossIndex = getRightTileIndex(index);
                	break;
                case 2:
                	crossIndex = getUpTileIndex(index);
                	break;
                case 3:
                	crossIndex = getLeftTileIndex(index);
                	break;
                case 4:
                	crossIndex = getDownTileIndex(index);
                	break;
                }
                
                if (crossIndex != -1) {
                    crossTile = getTile(crossIndex);
                    tile.getCrossed().add(crossIndex);
                    crossTile.getCrossed().add(index);
                }
            }
        }
    }
    
    public class SortByNearCenter implements Comparator<Integer> {

		@Override
		public int compare(Integer o1, Integer o2) {
			int lrow = getTileRow(o1);
	        int lcol = getTileCol(o1);
	        int lmin = Math.min(Math.abs(lrow - maxRow / 2), Math.abs(lcol - maxCol / 2));
	        int rrow = getTileRow(o2);
	        int rcol = getTileCol(o2);
	        int rmin = Math.min(Math.abs(rrow - maxRow / 2), Math.abs(rcol - maxCol / 2));
	        if (lmin < rmin) {
	            return -1;
	        } else if (lmin > rmin) {
	            return 1;
	        } else {
	            return 0;
	        }
		}
    	
    }

    public void randomGenerateB() {
        // @xl 第二步，把互不相同的路径，连接在一起 //
        ArrayList<Integer> allCrossedList = new ArrayList<Integer>();
        for (int i = 0; i < maxRow * maxCol; ++i) {
            Tile tile = getTile(i);
            if (tile.isValid()) {
                allCrossedList.add(i);
            }
        }

        ArrayList<Integer> connectList = new ArrayList<Integer>();
        ArrayList<Integer> notConnectList = new ArrayList<Integer>(allCrossedList);
        notConnectList.sort(new SortByNearCenter());
        Queue<Integer> todoQueue = new LinkedList<Integer>();
        Integer first = allCrossedList.get(0);
        todoQueue.add(first);
        Tile connectTile = getTile(first);
        int tryCount = maxTry;
        while (notConnectList.size() > 0 && --tryCount > 0) {
            while (todoQueue.size() > 0) {
                Integer curr = todoQueue.poll();
                connectTile = getTile(curr);
                if (connectTile == null){
                    //Debug.Log("null" + curr.ToString());
                	continue;
                }
                connectList.add(curr);
                notConnectList.remove(curr);
                for (Integer crossIndex : connectTile.getCrossed())
                {
                    if (!connectList.contains(crossIndex))
                    {
                        todoQueue.add(crossIndex);
                        if (crossIndex == -1) {
                            //Debug.Log("enqueue1 -1");
                        }
                    }
                }
            }
            if (notConnectList.size() > 0) {
            	Integer nc = notConnectList.get(0);
                int wantConnectIndex = -1;
                if (connectList.contains(getRightTileIndex(nc))) {
                    wantConnectIndex = getRightTileIndex(nc);
                } else if (connectList.contains(getUpTileIndex(nc))) {
                    wantConnectIndex = getUpTileIndex(nc);
                } else if (connectList.contains(getLeftTileIndex(nc))) {
                    wantConnectIndex = getLeftTileIndex(nc);
                } else if (connectList.contains(getDownTileIndex(nc))) {
                    wantConnectIndex = getDownTileIndex(nc);
                } if (wantConnectIndex == -1) {
                    notConnectList.remove(0);
                    notConnectList.add(nc);
                } else {
                    Tile tempTile = getTile(nc);
                    tempTile.getCrossed().add(wantConnectIndex);
                    Tile wantConnectedTile = getTile(wantConnectIndex);
                    if (wantConnectedTile != null) {
                        wantConnectedTile.getCrossed().add(nc);
                    }
                    todoQueue.add(nc);
                    if (nc == -1) {
                        //Debug.Log("enqueue2 -1");
                    }
                }
            }
        }
    }
    
    public void randomGenerateC() {
        // @xl 第三步，看是否需要回路 //
    	int tryCount = maxTry;
        while (--tryCount > 0) {
            int ranRow = new Random(System.currentTimeMillis()).nextInt(maxRow - 2) + 1;
            int ranCol = new Random(System.currentTimeMillis()).nextInt(maxCol - 2) + 1;
            int ranIndex = ranRow * maxCol + ranCol;
            Integer rightIndex = getRightTileIndex(ranIndex);
            Tile rightTile = getTile(rightIndex);
            Integer upIndex = getUpTileIndex(ranIndex);
            Tile upTile = getTile(upIndex);
            Integer leftIndex = getLeftTileIndex(ranIndex);
            Tile leftTile = getTile(leftIndex);
            Integer downIndex = getDownTileIndex(ranIndex);
            Tile downTile = getTile(downIndex);
            Tile tile = getTile(ranIndex);

            if (ranCol < maxCol / 2 && !tile.getCrossed().contains(rightIndex) 
            		&& rightTile != null && rightTile.getCrossed().size() > 0) {
                tile.getCrossed().add(rightIndex);
                rightTile.getCrossed().add(ranIndex);
                break;
            } else if (ranRow >= maxRow / 2 && !tile.getCrossed().contains(upIndex) 
            		&& upTile != null && upTile.getCrossed().size() > 0) {
                tile.getCrossed().add(upIndex);
                upTile.getCrossed().add(ranIndex);
                break;
            } else if (ranCol >= maxCol / 2 && !tile.getCrossed().contains(leftIndex)
            		&& leftTile != null && leftTile.getCrossed().size() > 0) {
                tile.getCrossed().add(leftIndex);
                leftTile.getCrossed().add(ranIndex);
                break;
            } else if (ranRow < maxRow / 2 && !tile.getCrossed().contains(downIndex) 
            		&& downTile != null && downTile.getCrossed().size() > 0) {
                tile.getCrossed().add(downIndex);
                downTile.getCrossed().add(ranIndex);
                break;
            }

        }
    }

    public void randomGenerate1() {
        randomGenerateA();
        randomGenerateB();
        randomGenerateC();
        // @xl 检查合法性，否则重新生成 //
        int outerTileCount = 0;
        for (int i = 0; i < maxRow; ++i) {
            Tile tile = getTile(i, 0);
            if (tile.isValid()) {
                ++outerTileCount;
            }
            tile = getTile(i, maxCol - 1);
            if (tile.isValid()) {
                ++outerTileCount;
            }
            tile = getTile(0, i);
            if (tile.isValid()) {
                ++outerTileCount;
            }
            tile = getTile(maxRow - 1, i);
            if (tile.isValid()) {
                ++outerTileCount;
            }
        }

        if (outerTileCount < 3) {
            randomGenerate1();
        }
    }

    public void calcTileCrossType() {
        for (int i = 0; i < maxRow * maxCol; ++i) {
            Tile tile = getTile(i);
            if (tile.isValid()) {
                boolean upCross = tile.getCrossed().contains(getUpTileIndex(i));
                boolean leftCross = tile.getCrossed().contains(getLeftTileIndex(i));
                boolean downCross = tile.getCrossed().contains(getDownTileIndex(i));
                boolean rightCross = tile.getCrossed().contains(getRightTileIndex(i));
                int mask = 0;
                if (rightCross) {
                    mask |= 0x01;
                } 
                if (upCross) {
                    mask |= 0x02;
                }
                if (leftCross) {
                    mask |= 0x04;
                }
                if (downCross) {
                    mask |= 0x08;
                }
                tile.setCrossMask(mask);
            }
        }
    }
}
