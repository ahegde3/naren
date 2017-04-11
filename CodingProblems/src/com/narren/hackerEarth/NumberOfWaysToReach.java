package com.narren.hackerEarth;
/**
 * Problem Statement :
Given a 2-D matrix with M rows and N columns, find the number of ways to reach cell with coordinates (i,j)
from starting cell (0,0) under the condition that you can only travel one step right or one step down.
 * @author naren
 *
 */
public class NumberOfWaysToReach {

	int x;
	int y;
	
	public static void main(String[] args) {
		NumberOfWaysToReach nrw = new NumberOfWaysToReach();
		int[][] grid = new int[3][3];
		nrw.x = 2;
		nrw.y = 2;
		System.out.println(nrw.getWays(grid));
	}
	int getWays(int[][] grid) {
		grid[x - 1][y] = 1;
		grid[x][y - 1] = 1;
		
		for(int i = x; i >= 0; i--) {
			for(int j = y; j >= 0; j--) {
				if(i == x && j == y) {
					continue;
				}
				if(grid[i][j] > 0) {
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
				grid[i][j] = right + down;
			}
		}
		
		return grid[0][0];
	}
}
