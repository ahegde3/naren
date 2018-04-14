package com.narren.leetCode;

public class UniquePaths2 {
	public int uniquePaths(int m, int n) {
		int[][] arr = new int[m][n];
		arr[m-1][n-1] = 0;

		if(n-2 >= 0) {
			arr[m-1][n-2] = 1;	
		}

		if(m-2 >= 0) {
			arr[m-2][n-1] = 1;        	
		}
		for(int i = m - 1; i >= 0; i--) {
			for(int j = n - 1; j >= 0; j--) {
				int r = j + 1 >= n ? 0 : arr[i][j+1];
				int d = i + 1 >= m ? 0 : arr[i+1][j];
				arr[i][j] = arr[i][j] == 0 ? r + d : arr[i][j];
			}
		}

		return arr[0][0];
	}

	public static void main(String[] args) {
		UniquePaths2 up2 = new UniquePaths2();
		int r = up2.uniquePaths(1, 2);
		System.out.println(r);
	}
}
