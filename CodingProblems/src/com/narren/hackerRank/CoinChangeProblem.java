package com.narren.hackerRank;

import java.util.Scanner;

public class CoinChangeProblem {
/**
 * 
 * https://www.hackerrank.com/challenges/coin-change
 * Sample Input
4 3
1 2 3
Output : 4

10 4
2 5 3 6

Output : 5
 */
	static long[] coin;
	static long[][] arr;
	static boolean[][] res;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int M = sc.nextInt();
		coin = new long[M];
		arr = new long[M][N + 1];
		res = new boolean[M][N + 1];
		for(int i = 0; i < M; i++) {
			coin[i] = sc.nextInt();
		}
		process(M - 1, N);
		System.out.println(arr[M - 1][N]);
	}

	static long process(int i, int j) {
		if(i < 0 || j < 0) {
			return 0;
		}
		if (j == 0) {
			return 1;
		}
		if (res[i][j]) {
			return arr[i][j];
		}

		if (i == 0 && coin[i] > j) {
			return 0;
		}

		arr[i][j] = process(i, (int)(j - coin[i])) + process(i - 1, j);
		res[i][j] = true;
		return arr[i][j];
	}

}
