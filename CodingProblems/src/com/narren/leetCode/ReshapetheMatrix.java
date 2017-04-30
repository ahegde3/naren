package com.narren.leetCode;

/**
 * 
In MATLAB, there is a very useful function called 'reshape', which can reshape a matrix into a new one with different size but keep its original data.

You're given a matrix represented by a two-dimensional array, and two positive integers r and c representing the row number and column number of the wanted reshaped matrix, respectively.

The reshaped matrix need to be filled with all the elements of the original matrix in the same row-traversing order as they were.

If the 'reshape' operation with given parameters is possible and legal, output the new reshaped matrix; Otherwise, output the original matrix.

Example 1:
Input: 
nums = 
[[1,2],
 [3,4]]
r = 1, c = 4
Output: 
[[1,2,3,4]]
Explanation:
The row-traversing of nums is [1,2,3,4]. The new reshaped matrix is an 1 * 4 matrix, fill it row by row by using the previous list.
Example 2:
Input: 
nums = 
[[1,2],
 [3,4]]
r = 2, c = 4
Output: 
[[1,2],
 [3,4]]
Explanation:
There is no way to reshape a 2 * 2 matrix to a 2 * 4 matrix. So output the original matrix.
Note:
The height and width of the given matrix is in range [1, 100].
The given r and c are all positive.

https://leetcode.com/contest/leetcode-weekly-contest-30/problems/reshape-the-matrix/
 * 
 * @author naren
 *
 */
public class ReshapetheMatrix {

	public static void main(String[] args) {
		ReshapetheMatrix rsm = new ReshapetheMatrix();
		int res[][] = rsm.matrixReshape(new int[][]
				{
			{1, 2},
			{3, 4}}, 1, 4);
		System.out.println();
	}
	public int[][] matrixReshape(int[][] nums, int r, int c) {
		if((r * c) != (nums.length * nums[0].length)) {
			return nums;
		}
		int[][] res = new int[r][c];

		int k = 0, l = 0;

		for(int i = 0; i < r; i++) {
			for(int j = 0; j < c; j++) {
				if(l >= nums[0].length) {
					l = 0;
					k++;
				}
				res[i][j] = nums[k][l];
				l++;
			}
		}
		return res;

	}

}
