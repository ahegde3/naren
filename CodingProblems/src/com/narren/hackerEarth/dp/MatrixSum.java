package com.narren.hackerEarth.dp;

import java.util.Scanner;

public class MatrixSum {

	static int[][] dp;
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int M = sc.nextInt();
		dp = new int[N][M];
		int[][] in = new int[N][M];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				in[i][j] = sc.nextInt();
			}
		}
		int cases = sc.nextInt();
		while(cases > 0) {
			int x = sc.nextInt();
			int y = sc.nextInt();
			System.out.println(findSum(in, x, y));
			cases--;
		}
	}
	static int findSum(int[][] matrix, int x, int y) {
		if(x < 0 || y < 0 || x >= matrix.length || y >= matrix[0].length) {
			return 0;
		}
		
		if(dp[x][y] > 0) {
			return dp[x][y];
		}
	}
}
