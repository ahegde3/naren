package com.narren.leetCode;

public class EggDropingPuzzle {

	
	public static void main(String[] args) {
		EggDropingPuzzle edp = new EggDropingPuzzle();
		System.out.println(edp.minAttempts(100, 2));
				
	}
	// t floors, k eggs
	int minAttempts(int t, int k) {
		int[][] dp = new int[t + 1][k + 1];
		
		for(int i = 0; i <= t; i++ ) {
			dp[i][0] = 0;
			dp[i][1] = i;
		}
		
		
		for(int i = 0 ; i <= k; i++) {
			dp[0][i] = 0;
		}
		
		for(int i = 1; i <= k; i++) {
			dp[1][i] = 1;
		}
		
		for(int i = 2; i <= k; i++) {
			for(int j = 2; j <= t; j++) {
				dp[j][i] = Integer.MAX_VALUE;
				int res = 0;
				for(int y = 1; y <= j; y++) {
					res = 1 + Math.max(dp[y - 1][i - 1], dp[j - y][i]);
					dp[j][i] = Math.min(res, dp[j][i]);
				}
			}
		}
		return dp[t][k];
		
	}
	
}
