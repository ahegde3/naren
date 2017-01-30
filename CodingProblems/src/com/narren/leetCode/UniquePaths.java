package com.narren.leetCode;
/**
 * 
A robot is located at the top-left corner of a m x n grid (marked 'Start' in the diagram below).

The robot can only move either down or right at any point in time. The robot is trying to reach
the bottom-right corner of the grid (marked 'Finish' in the diagram below).

How many possible unique paths are there?


Above is a 3 x 7 grid. How many possible unique paths are there?

Note: m and n will be at most 100.
 *
https://leetcode.com/problems/unique-paths/
 * 
 * @author naren
 *
 */
public class UniquePaths {

	public static void main(String[] args) {
		UniquePaths uq = new UniquePaths();
		System.out.println(uq.uniquePaths(2, 2));
	}
	public int uniquePaths(int m, int n) {
		int[][] visited = new int[m][n];
		visited[m - 1][n - 1] = 1;
		countPaths(visited, m, n, 0, 0);
		return visited[0][0];

	}

	void countPaths(int[][] visited, int m, int n, int curRow, int curCol) {
		if(curRow < 0 || curCol < 0 || curRow >= m || curCol >= n) {
			return;
		}
		if(curRow == m - 1 && curCol == n - 1) {
			return;
		}
		if(visited[curRow][curCol] > 0) {
			return;
		}
		countPaths(visited, m, n, curRow, curCol + 1); // Right
		countPaths(visited, m, n, curRow + 1, curCol); // Down
		visited[curRow][curCol] = (curCol + 1 < n ? visited[curRow][curCol + 1] : 0) + (curRow + 1 < m ? visited[curRow + 1][curCol] : 0);

	}
}
