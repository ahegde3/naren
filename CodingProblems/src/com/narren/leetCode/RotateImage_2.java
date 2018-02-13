package com.narren.leetCode;

public class RotateImage_2 {

	public void rotate(int[][] matrix) {
		
		int row = matrix.length;
		int col = matrix[0].length;
		
		for(int i = 0; i < row; i++) {
			int t = matrix[i][i];
			for(int j = i + 1; j < col; j++) {
				System.out.println(matrix[i][j]);
			}
		}

	}
	
	public static void main(String[] args) {
		RotateImage_2 ri2 = new RotateImage_2();
		int[][] matrix = new int[][]{{1,2,3}, {4,5,6}, {7,8,9}};
		ri2.rotate(matrix);
	}
}
