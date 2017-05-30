package com.narren.leetCode;

/**
 * 
Given an m * n matrix M initialized with all 0's and several update operations.

Operations are represented by a 2D array, and each operation is represented by an array with two positive integers a and b, which means M[i][j] should be added by one for all 0 <= i < a and 0 <= j < b.

You need to count and return the number of maximum integers in the matrix after performing all the operations.

Example 1:
Input: 
m = 3, n = 3
operations = [[2,2],[3,3]]
Output: 4
Explanation: 
Initially, M = 
[[0, 0, 0],
 [0, 0, 0],
 [0, 0, 0]]

After performing [2,2], M = 
[[1, 1, 0],
 [1, 1, 0],
 [0, 0, 0]]

After performing [3,3], M = 
[[2, 2, 1],
 [2, 2, 1],
 [1, 1, 1]]

So the maximum integer in M is 2, and there are four of it in M. So return 4.
Note:
The range of m and n is [1,40000].
The range of a is [1,m], and the range of b is [1,n].
The range of operations size won't exceed 10,000.
 * 
 * @author naren
 *
 */
public class RangeAdditionII {
	public static void main(String[] args) {
		System.out.println(new RangeAdditionII().maxCount(39000, 39000, new int[][]{{1}}));
	}
	public int maxCount(int m, int n, int[][] ops) {
		if(ops.length < 1 || ops[0].length == 0) {
			return m * n;
		}
		int[][] arr = new int[m][n];
		
		int max = Integer.MIN_VALUE;
		int count = 0;
		for(int[] op : ops) {
			for(int i = 0 ; i < op[0]; i++) {
				for(int j = 0 ; j < op[1]; j++) {
					arr[i][j] += 1;
					max = Math.max(max, arr[i][j]);
					}
				}
			}
		if(max == Integer.MIN_VALUE) {
			return m * n;
		}
		for(int i = 0 ; i < m; i++) {
			for(int j = 0 ; j < n; j++) {
				if(max == arr[i][j]) {
					count++;
				}
			}
		}
		return count;

	}
}
