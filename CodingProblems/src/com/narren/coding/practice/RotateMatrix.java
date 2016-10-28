package com.narren.coding.practice;

public class RotateMatrix {

	public static void main(String[] args) {
		int temp = 1;
		int[][] matrix = new int[5][5];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j] = temp++;
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
		
		rotateMatrix(matrix);
		
		System.out.println("@@@@@@@@@@@");
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}
	static private void rotateMatrix(int[][] matrix) {
		int matLen = matrix.length;
		for (int layer = 0; layer < matLen/2; layer++) {
			int topX = layer;
			int topY = layer;
			
			int rightX = layer;
			int rightY = (matLen - 1) - layer;
			
			int bottomX = (matLen - 1) - layer;
			int bottomY = (matLen - 1) - layer;
			
			int leftX = (matLen - 1) - layer;
			int leftY = layer;
			
			for (int i = 0; i < (matLen - 1) - 2*layer; i++) {
				int temp1 = matrix[rightX][rightY];
				matrix[rightX][rightY] = matrix[topX][topY];
				
				int temp2 = matrix[bottomX][bottomY];
				matrix[bottomX][bottomY] = temp1;
				
				temp1 = matrix[leftX][leftY];
				matrix[leftX][leftY] = temp2;
				matrix[topX][topY] = temp1;		
				
				topY += 1;
				rightX += 1;
				bottomY -= 1;
				leftX -= 1;
				System.out.println("@@@@@@@@@@@");
				
				for (int k = 0; k < matrix.length; k++) {
					for (int l = 0; l < matrix.length; l++) {
						System.out.print(matrix[k][l] + " ");
					}
					System.out.println();
				}
			}
			System.out.println("LAYER CHANGEE");
		}
		
	}
}
