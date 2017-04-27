package com.narren.hackerEarth.dp;

import java.util.Scanner;

public class MatrixSum {

	public static void main(String[] args) {{
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int M = sc.nextInt();
		long[][]dp = new long[N][M];
		long[][] in = new long[N][M];
		long curSum = 0;
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				in[i][j] = sc.nextInt();
				curSum += in[i][j];
				dp[i][j] = curSum;
			}
		}
		int cases = sc.nextInt();
		while(cases > 0) {
			int x = sc.nextInt();
			int y = sc.nextInt();
			System.out.println(dp[x - 1][y - 1]);
			cases--;
		}
	}}
}
