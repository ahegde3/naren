package com.narren.leetCode;
/**
 * 
 * Given a 01 matrix M, find the longest line of consecutive one in the matrix. The line could be horizontal, vertical, diagonal or anti-diagonal.

Example:
Input:
[[0,1,1,0],
 [0,1,1,0],
 [0,0,0,1]]
Output: 3
Hint: The number of elements in the given matrix will not exceed 10,000.

https://leetcode.com/contest/leetcode-weekly-contest-29/problems/longest-line-of-consecutive-one-in-matrix/
 * 
 * @author naren
 *
 */
public class LongestLineofConsecutiveOneinMatrix {

	public static void main(String[] args) {
		int[][] cost = new int[][]{
			{0, 1, 1, 0},
			{0, 0, 1, 0},
			{0, 1, 0, 0},
			{1, 0, 0, 0}}; 
			
			System.out.println(new LongestLineofConsecutiveOneinMatrix().longestLine(cost));
	}
	public int longestLine(int[][] M) {

		int max = 0;
		for(int i = 0; i < M.length; i++) {
			for(int j = 0; j < M[0].length; j++) {
				if(M[i][j] == 1) {
					max = Math.max(max, getMax(M, i, j));
				}
			}
		}
		
		return max;
	}
	
	int getMax(int[][] M, int i, int j) {
		int max = 0;
		if(j == 0 || M[i][j - 1] == 0) {
			max = rightMax(M, i, j + 1, 1);
		}
		if(i == 0 || M[i - 1][j] == 0) {
			max = Math.max(max, downMax(M, i + 1, j, 1));
		}
		if(j == 0 || i == 0 || M[i - 1][j - 1] == 0) {
			max = Math.max(max, diagRMax(M, i + 1, j + 1, 1));
		}
		if(j == M[0].length - 1 || i == 0 || M[i - 1][j + 1] == 0) {
			max = Math.max(max, diagLMax(M, i + 1, j - 1, 1));
		}
		
		return max;
	}
	
	int rightMax(int[][] M, int i, int j, int count) {
		if(j >= M[0].length) {
			return count;
		}
		if(M[i][j] == 0) {
			return count;
		}
		return rightMax(M, i, j + 1, count + 1);
	}
	
	int downMax(int[][] M, int i, int j, int count) {
		if(i >= M.length) {
			return count;
		}
		if(M[i][j] == 0) {
			return count;
		}
		return downMax(M, i + 1, j, count + 1);
	}
	
	int diagRMax(int[][] M, int i, int j, int count) {
		if(i >= M.length || j >= M[0].length) {
			return count;
		}
		if(M[i][j] == 0) {
			return count;
		}
		return diagRMax(M, i + 1, j + 1, count + 1);
	}
	
	int diagLMax(int[][] M, int i, int j, int count) {
		if(i >= M.length || j < 0) {
			return count;
		}
		if(M[i][j] == 0) {
			return count;
		}
		return diagLMax(M, i + 1, j - 1, count + 1);
	}
}
