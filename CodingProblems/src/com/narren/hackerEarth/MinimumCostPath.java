package com.narren.hackerEarth;

/**
 * Problem Statement :
Given a cost matrix Cost[][] where Cost[i][j] denotes the Cost of visiting cell with coordinates (i,j),
find a min-cost path to reach a cell (x,y) from cell (0,0) under the condition that you can only travel one step
right or one step down. (We assume that all costs are positive integers)
 * @author naren
 *
 */
public class MinimumCostPath {

	int x;
	int y;
	int[][] visited;

	public static void main(String[] args) {
		MinimumCostPath mcp = new MinimumCostPath();
		int[][] cost = new int[][]{
			{1, 2, 5},
			{3, 4, 7},
			{4, 6, 9}};
			mcp.x = 2;
			mcp.y = 2;
			mcp.visited = new int[3][3];
			System.out.println(mcp.getMinPath(cost, 0, 0));

	}

	int getMinPath(int[][] cost, int curI, int curJ) {
		if(curI > x || curJ > y) {
			return Integer.MAX_VALUE;
		}
		if(curI == x && curJ == y) {
			return cost[x][y];
		}

		if(visited[curI][curJ] > 0) {
			return visited[curI][curJ];
		}

		visited[curI][curJ] = Math.min(getMinPath(cost, curI, curJ + 1), getMinPath(cost, curI + 1, curJ)) + cost[curI][curJ];
		return visited[curI][curJ];
	}
}
