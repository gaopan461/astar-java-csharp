package pathFinding;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AStarDataMgr {
	// astar寻路文件的路径
	private static final String ASTAR_DIR = "astar/";
	
	// astar寻路文件的后缀
	private static final String INFO_SUFFIX = ".txt";
	
	private static Map<String, AStarData> astarDatas = new HashMap<>();
		
	static {
		init();
	}
	
	private static void init() {
		File dir = new File(ASTAR_DIR);
		if(!dir.isDirectory()) {
			return;
		}
		
		File[] files = dir.listFiles();
		for(File file : files) {
			String fileName = file.getName();
			if(!fileName.endsWith(INFO_SUFFIX)) {
				continue;
			}
			
			String tileId = fileName.replaceAll(INFO_SUFFIX, "");
			AStarData astarData = new AStarData(file);
			astarDatas.put(tileId, astarData);
		}
	}
	
	public static AStarData getAstarData(String tileId) {
		return astarDatas.get(tileId);
	}
}
