package pathFinding.heuristics;

/**
 * The interface AStar Heuristics have to implement.
 */
public interface AStarHeuristic {

	/**
	 * 
	 * The heuristic tries to guess how far a given Cell is from the goal Cell.
	 * The lower the cost, the more likely a Cell will be searched next.
	 * 
	 * @param map The map on which we try to find the path
	 * @param startX The x coordinate of the tile being evaluated
	 * @param startY The y coordinate of the tile being evaluated
	 * @param goalX The x coordinate of the target location
	 * @param goalY The y coordinate of the target location
	 * @return The cost associated with the given tile
	 */
	public float getEstimatedDistanceToGoal(int startX, int startY, int goalX, int goalY);
}
