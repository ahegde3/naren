package com.narren.hackerEarth.dp;

import java.util.Scanner;

public class MatrixSum {

	public static void main(String[] args) {
		long s = System.currentTimeMillis();
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int M = sc.nextInt();
		long[][]dp = new long[N][M];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				long n = sc.nextInt();
				populateDp(i, j, n, dp);
			}
		}
		int cases = sc.nextInt();
		while(cases > 0) {
			int x = sc.nextInt();
			int y = sc.nextInt();
			System.out.println(dp[x - 1][y - 1]);
			cases--;
		}
		System.out.println("Total time=" + (System.currentTimeMillis() - s));
	}
	
	static void populateDp(int i, int j, long num, long[][] dp) {
		dp[i][j] = num + getVal(i - 1, j, dp) + getVal(i, j - 1, dp) - getVal(i - 1, j - 1, dp);
	}
	
	static long getVal(int i, int j, long[][] dp) {
		if(i < 0 || j < 0 || i >= dp.length || j >= dp[0].length) {
			return 0;
		}
		return dp[i][j];
	}

}
