using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AStar.astar
{
    class AStarData
    {
        private const String SPLIT_SUFFIX = ",";
	
	    private int widthInCells;
	    private int heightInCells;
	    private float cellSize;
	    private int[,] obstacleInfo;
	    private float[,] heightInfo;
	
	    public AStarData(String file)
        {
		    String fileName = System.IO.Path.GetFileName(file);
            StreamReader sr = new StreamReader(file, Encoding.Default);
		
		    try
            {			
			    String[] headerInfo = sr.ReadLine().Split(SPLIT_SUFFIX.ToCharArray());
			    if(headerInfo.Length != 3)
                {
				    throw new ApplicationException("astar文件头数据不合法，文件:" + fileName);
			    }
			
			    widthInCells = int.Parse(headerInfo[0]);
			    heightInCells = int.Parse(headerInfo[1]);
			    cellSize = float.Parse(headerInfo[2]);
			    obstacleInfo = new int[heightInCells, widthInCells];
			    heightInfo = new float[heightInCells, widthInCells];
			
			    for(int h = 0; h < heightInCells; ++h)
                {
				    String[] lineData = sr.ReadLine().Split(SPLIT_SUFFIX.ToCharArray());
				    if(lineData.Length < widthInCells * 2)
                    {
                        throw new ApplicationException("astar寻路文件错误：读取文件错误。文件:" + fileName + ",行:" + (h + 2));
				    }
				
				    for(int w = 0; w < widthInCells; ++w)
                    {
					    obstacleInfo[h, w] = int.Parse(lineData[w*2]);
					    heightInfo[h, w] = float.Parse(lineData[w*2+1]);
				    }
			    }
		    }
            catch (IOException e)
            {
                throw new ApplicationException("astar寻路文件错误：读取文件错误。文件:" + fileName);
		    }
            finally
            {
                sr.Close();
		    }
	    }

	    public int getWidthInCells()
        {
		    return widthInCells;
	    }

	    public int getHeightInCells()
        {
		    return heightInCells;
	    }

	    public float getCellSize()
        {
		    return cellSize;
	    }
	
	    public int[,] getObstacleInfo()
        {
		    return obstacleInfo;
	    }

	    public float[,] getHeightInfo()
        {
		    return heightInfo;
	    }

	    public int getObstacle(int x, int y)
        {
		    try
            {
			    return obstacleInfo[y, x];
		    }
            catch (Exception e)
            {
			    return 1;
		    }
	    }
	
	    public float getHeight(int x, int y)
        {
		    try
            {
			    return heightInfo[y, x];
		    }
            catch (Exception e)
            {
			    return 0f;
		    }
	    }
    }
}
