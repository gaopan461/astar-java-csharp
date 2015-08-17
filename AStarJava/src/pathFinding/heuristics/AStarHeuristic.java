package pathFinding.heuristics;

import java.awt.Point;

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
	 * @param x The x coordinate of the tile being evaluated
	 * @param y The y coordinate of the tile being evaluated
	 * @param tx The x coordinate of the target location
	 * @param ty The y coordinate of the target location
	 * @return The cost associated with the given tile
	 */
	public float getEstimatedDistanceToGoal(Point start, Point goal);
}
