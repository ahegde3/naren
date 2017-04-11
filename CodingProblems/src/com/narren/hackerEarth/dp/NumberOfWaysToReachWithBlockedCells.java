package com.narren.hackerEarth.dp;
/**
 * 
 * Problem Statement :
A robot is designed to move on a rectangular grid of M rows and N columns. The robot is initially positioned at (1, 1), i.e., the top-left cell.
The robot has to reach the (M, N) grid cell. In a single step, robot can move only to the cells to its immediate east and south directions.
That means if the robot is currently at (i, j), it can move to either (i + 1, j) or (i, j + 1) cell,
provided the robot does not leave the grid. Now somebody has placed several obstacles in random positions on the grid, through which the robot cannot pass.
Given the positions of the blocked cells, your task is to count the number of paths that the robot can take to move from (1, 1) to (M, N).

Input is three integers M, N and P denoting the number of rows, number of columns and number of blocked cells respectively.
In the next P lines, each line has exactly 2 integers i and j denoting that the cell (i, j) is blocked.
 * @author naren
 *
 */
public class NumberOfWaysToReachWithBlockedCells {

	int x;
	int y;
	
	public static void main(String[] args) {
		NumberOfWaysToReachWithBlockedCells mcp = new NumberOfWaysToReachWithBlockedCells();
		int[][] cost = new int[][]{
			{0, 0, 0},
			{0, -1, 0},
			{0, 0, 0}};
			mcp.x = 2;
			mcp.y = 2;
			System.out.println(mcp.getWays(cost));

	}
	
	int getWays(int[][] grid) {
		if(grid[x - 1][y] != -1) {
			grid[x - 1][y] = 1;			
		}
		
		if(grid[x][y - 1] != -1 ) {
			grid[x][y - 1] = 1;
		}
		
		
		for(int i = x; i >= 0; i--) {
			for(int j = y; j >= 0; j--) {
				if(i == x && j == y) {
					continue;
				}
				if(grid[i][j] > 0 || grid[i][j] == -1) {
					continue;
				}
				int down = 0;
				if(i + 1 <= x) {
					down = grid[i + 1][j];
				}
				int right = 0;
				if(j + 1 <= y) {
					right = grid[i][j + 1];
				}
				if(down != -1) {
					grid[i][j] += down;	
				}
				
				if(right != -1) {
					grid[i][j] += right;	
				}
			}
		}
		
		return grid[0][0];
	}
}
