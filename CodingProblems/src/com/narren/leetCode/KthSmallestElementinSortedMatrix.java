package com.narren.leetCode;

/**
 * 
Given a n x n matrix where each of the rows and columns are sorted in ascending order, find the kth smallest element in the matrix.

Note that it is the kth smallest element in the sorted order, not the kth distinct element.

Example:

matrix = [
   [ 1,  5,  9],
   [10, 11, 13],
   [12, 13, 15]
],
k = 8,

return 13.
Note: 
You may assume k is always valid, 1 ≤ k ≤ n2.

https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/
 * 
 * @author naren
 *
 */
public class KthSmallestElementinSortedMatrix {

	public int kthSmallest(int[][] matrix, int k) {
		int[] res = new int[matrix.length * matrix.length];
		int index = 0;
		
		for(int i = 0; i < matrix.length; i++) {
			res[index++] = matrix[0][i];
		}
		
		for(int i = 1; i < matrix.length; i++)
			for(int j = 0; j < matrix.length; j++) {
				int e = matrix[i][j];
				int t = index - 1;
				while(res[t] > e && t - 1> 0) {
					res[t + 1] = res[t];
					t--;
				}
				res[t + 1] = e;
				index++;
			}
		return res[k - 1];
	}
}
