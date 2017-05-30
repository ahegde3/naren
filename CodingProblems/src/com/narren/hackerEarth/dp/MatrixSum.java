package com.narren.hackerEarth.dp;

import java.util.Scanner;

public class MatrixSum {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int M = sc.nextInt();
		long[][]dp = new long[N][M];
		long[][] arr = new long[N][M];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				arr[i][j] = sc.nextInt();
			}
		}
		dp[0][0] = arr[0][0];

		for(int i = 1  ; i < N ; ++i)
			dp[i][0] = dp[i-1][0] + arr[i][0];

		for(int i = 1  ; i < M ; ++i)
			dp[0][i] = dp[0][i-1] + arr[0][i];

		for(int i = 1; i < N; i++) {
			for(int j = 1; j < M; j++) {
				dp[i][j] = arr[i][j] + dp[i - 1][j] + dp[i][j - 1] - dp[i - 1][j - 1];
			}
		}
		int cases = sc.nextInt();
		while(cases > 0) {
			int x = sc.nextInt();
			int y = sc.nextInt();
			System.out.println(dp[x - 1][y - 1]);
			cases--;
		}
	}
}
