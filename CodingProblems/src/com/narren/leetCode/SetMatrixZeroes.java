package com.narren.leetCode;

/**
 * Given a m x n matrix, if an element is 0, set its entire row and column to 0. Do it in-place.
 * 
 * Example 1:

Input: 
[
  [1,1,1],
  [1,0,1],
  [1,1,1]
]
Output: 
[
  [1,0,1],
  [0,0,0],
  [1,0,1]
]
Example 2:

Input: 
[
  [0,1,2,0],
  [3,4,5,2],
  [1,3,1,5]
]
Output: 
[
  [0,0,0,0],
  [0,4,5,0],
  [0,3,1,0]
]


Follow up:

A straight forward solution using O(mn) space is probably a bad idea.
A simple improvement uses O(m + n) space, but still not the best solution.
Could you devise a constant space solution?
 * 
 * @author naren
 *
 */
public class SetMatrixZeroes {
	
	public static void main(String[] args) {
		// {1}, {0}, {3}
		// {1}, {0}
		// {1, 1, 1}, {0, 1, 2}
		// {4, 2, 6, 7, 0}, {8, 6, 8, 6, 0}, {2, 2, 9, 6, 10}
		int[][] matrix = new int[][]{{4, 2, 6, 7, 0}, {8, 6, 8, 6, 0}, {2, 2, 9, 6, 10}};
		setZeroes(matrix);
		for(int i = 0 ; i < matrix.length; i++) {
			for(int j = 0; j < matrix[0].length; j++) {
				System.out.print("" + matrix[i][j] + ", ");
			}
			System.out.println();
		}
	}

	public static void setZeroes(int[][] matrix) {
		int rows = matrix.length;
		int cols = matrix[0].length;
		boolean columnZero = false;

		for(int i = 0 ; i < rows; i++) {
			if(matrix[i][0] == 0){
				columnZero = true;
			}

			for(int j = 1; j < cols; j++) {
				if(matrix[i][j] == 0) {
					matrix[0][j] = matrix[i][0] = 0;
				}
			}

		}

		for(int i = rows - 1; i >= 0; i--) {
			for(int j = cols - 1; j > 0; j--) {
				if((matrix[0][j] == 0 || matrix[i][0] == 0)) {
					matrix[i][j] = 0;
				}
			}
            if(columnZero) {
			matrix[i][0] = 0;
		}

		}

	}
}
