package pathFinding.core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import pathFinding.heuristics.AStarHeuristic;

public class AStar {
	private AStarMap map;
	private AStarHeuristic heuristic;
	/**
	 * openList The list of Cells not searched yet, sorted by their distance to the goal as guessed by our heuristic.
	 */
	private ArrayList<AStarCell> closedList = new ArrayList<AStarCell>();;
	private ArrayList<AStarCell> openList = new ArrayList<AStarCell>();;

	public AStar(AStarMap map, AStarHeuristic heuristic) {
		this.map = map;
		this.heuristic = heuristic;
	}

	public ArrayList<Point> calcShortestPath(int startX, int startY, int goalX, int goalY) {
		AStarCell startCell = map.getCell(startX, startY);
		AStarCell goalCell = map.getCell(goalX, goalY);
		
		//Check if the start cell is also an obstacle (if it is, it is impossible to find a path)
		if (AStarCell.isObstacle(startCell)) {
			return null;
		}
		
		//Check if the goal cell is also an obstacle (if it is, it is impossible to find a path there)
		if (AStarCell.isObstacle(goalCell)) {
			return null;
		}

		startCell.reset();
		startCell.setDistanceFromStart(0);
		closedList.clear();
		openList.clear();
		openList.add(startCell);

		//while we haven't reached the goal yet
		while(openList.size() != 0) {

			//get the first Cell from non-searched Cell list, sorted by lowest distance from our goal as guessed by our heuristic
			AStarCell current = openList.get(0);

			// check if our current Cell location is the goal Cell. If it is, we are done.
			if(current.getX() == goalX && current.getY() == goalY) {
				return reconstructPath(current);
			}

			//move current Cell to the closed (already searched) list
			openList.remove(current);
			closedList.add(current);

			//go through all the current Cell neighbors and calculate if one should be our next step
			for(AStarCell neighbor : map.getNeighborList(current)) {
				boolean neighborIsBetter;

				//if we have already searched this Cell, don't bother and continue to the next one 
				if (closedList.contains(neighbor))
					continue;

				// calculate how long the path is if we choose this neighbor as the next step in the path 
				float neighborDistanceFromStart = (current.getDistanceFromStart() + AStarMap.getDistanceBetween(current, neighbor));

				//add neighbor to the open list if it is not there
				if(!openList.contains(neighbor)) {
					neighbor.reset();
					openList.add(neighbor);
					neighborIsBetter = true;
					//if neighbor is closer to start it could also be better
				} else if(neighborDistanceFromStart < current.getDistanceFromStart()) {
					neighborIsBetter = true;
				} else {
					neighborIsBetter = false;
				}
				// set neighbors parameters if it is better
				if (neighborIsBetter) {
					neighbor.setPreviousCell(current);
					neighbor.setDistanceFromStart(neighborDistanceFromStart);
					neighbor.setHeuristicDistanceFromGoal(heuristic.getEstimatedDistanceToGoal(neighbor.getPoint(), goalCell.getPoint()));
					
					Collections.sort(openList);
				}
			}
		}
		return null;
	}

	private ArrayList<Point> reconstructPath(AStarCell cell) {
		ArrayList<Point> path = new ArrayList<Point>();
		while(!(cell.getPreviousCell() == null)) {
			path.add(0,cell.getPoint());
			cell = cell.getPreviousCell();
		}
		path.add(0, cell.getPoint());
		return path;
	}

}
