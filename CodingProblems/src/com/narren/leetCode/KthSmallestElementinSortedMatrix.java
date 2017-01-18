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
        int lower = matrix[0][0];
        int upper = matrix[matrix.length - 1][matrix.length - 1];
        while(lower < upper) {
            int mid = lower + ((upper - lower) >> 1);
            int count = countOfSmallNum(matrix, mid);
            if(count < k) {
            	lower = mid + 1;
            } else {
            	upper = mid;
            }
        }
        return upper;
    }

    int countOfSmallNum(int[][] matrix, int mid) {
        int count = 0;
        int i = matrix.length - 1;
        int j = 0;
        while(j < matrix.length && i >= 0) {
            if(matrix[i][j] <= mid) {
                count += i + 1;
                j++;
            } else {
                i--;
            }
        }
        return count;
    }
}
