package com.narren.hackerRank;

import java.util.Scanner;

/**
 * 
 * @author ns.bisht
 *
 */
public class SamsungKDonutsAdditionRule {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int T = in.nextInt();
		for (int q = 1; q <= T ; q++) {
			int N = in.nextInt();
			int K = in.nextInt();
			int[][] inArr = new int[N][N];
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					inArr[i][j] = in.nextInt();
				}
			}
			System.out.println("#" + q + " " + process(inArr, K, N));
		}
	}
	private static int process (int[][] inArr, int k, int N) {
		
		int startX = 0;
		int startY = 0;
		int res = 0;
		while ((startX + k) <= N) {
			while ((startY + k) <= N) {
				int sum = 0;
				int sub = 0;
				for (int i = startX; i < startX + k; i++) {
					for (int j = startY; j < startY + k; j++) {
						sum += inArr[i][j];
					}
				}
				for (int i = startX + 1; i < startX + (k - 1); i++) {
					for (int j = startY + 1; j < startY + (k - 1); j++) {
						sub += inArr[i][j];
					}
				}
				res = res < Math.abs(sum - sub) ? Math.abs(sum - sub) : res;
				//System.out.println("~" + res);
				startY++;
			}
			startY = 0;
			startX++;
		}
		return res;
	}
}
