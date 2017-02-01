package com.narren.leetCode;
/**
 * 
Given a m x n grid filled with non-negative numbers, find a path
from top left to bottom right which minimizes the sum of all numbers along its path.

Note: You can only move either down or right at any point in time.

https://leetcode.com/problems/minimum-path-sum/
 * 
 * @author nsbisht
 *
 */
public class MinimumPathSumO {

	public int minPathSum(int[][] grid) {
		int m = grid.length;
        int n = grid[0].length;
        int[][] visited = new int[m][n];

        visited[m - 1][n - 1] = grid[m - 1][n - 1];

        for(int i = m - 2; i >= 0; i--) {
            visited[i][n - 1] = visited[i + 1][n - 1] + grid[i][n - 1];
        }

        for(int i = n - 2; i >= 0; i--) {
            visited[m - 1][i] = visited[m - 1][i + 1] + grid[m - 1][i];
        }

        int row = m - 2;
        int col = n - 2;

        while(row >= 0 && col >= 0) {
            for(int i = row; i >= 0; i--) {
                visited[i][col] = Math.min(visited[i][col + 1], visited[i + 1][col]) + grid[i][col];
            }

            for(int i = col; i >= 0; i--) {
                visited[row][i] = Math.min(visited[row][i + 1], visited[row + 1][i]) + grid[row][i];
            }
            row--;
            col--;
        }


        return visited[0][0];
	}

	public static void main(String[] args) {
		MinimumPathSumO obj = new MinimumPathSumO();
		long start = System.currentTimeMillis();
		int[][] grid = new int[][]
                {{7,1,3,5,8,9,9,2,1,9,0,8,3,1,6,6,9,5},
                {9,5,9,4,0,4,8,8,9,5,7,3,6,6,6,9,1,6},
                {8,2,9,1,3,1,9,7,2,5,3,1,2,4,8,2,8,8},
                {6,7,9,8,4,8,3,0,4,0,9,6,6,0,0,5,1,4},
                {7,1,3,1,8,8,3,1,2,1,5,0,2,1,9,1,1,4},
                {9,5,4,3,5,6,1,3,6,4,9,7,0,8,0,3,9,9},
                {1,4,2,5,8,7,7,0,0,7,1,2,1,2,7,7,7,4},
                {3,9,7,9,5,8,9,5,6,9,8,8,0,1,4,2,8,2},
                {1,5,2,2,2,5,6,3,9,3,1,7,9,6,8,6,8,3},
                {5,7,8,3,8,8,3,9,9,8,1,9,2,5,4,7,7,7},
                {2,3,2,4,8,5,1,7,2,9,5,2,4,2,9,2,8,7},
                {0,1,6,1,1,0,0,6,5,4,3,4,3,7,9,6,1,9}};
          System.out.println(obj.minPathSum(grid));
          System.out.println(System.currentTimeMillis() - start);
	}
}
