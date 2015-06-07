package com.narren.hackerRank;

import java.util.Scanner;

public class TheGridSearch {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int T = in.nextInt();
		while (T > 0) {
			int R = in.nextInt();
			int C = in.nextInt();
			int[][] G = new int[R][C];
			for (int i = 0; i < R; i++) {
				String column = in.next();
				for (int j = 0; j < C; j++) {
					G[i][j] = Integer.parseInt(String.valueOf(column.charAt(j)));
				}
			}
			int r = in.nextInt();
			int c = in.nextInt();
			int[][] p = new int[r][c];
			for (int i = 0; i < r; i++) {
				String column = in.next();
				for (int j = 0; j < c; j++) {
					p[i][j] = Integer.parseInt(String.valueOf(column.charAt(j)));
				}
			}
			process(G, R, C, p, r, c);
			T--;
		}

	}
	
	static void process (int[][] G, int R, int C, int[][] P, int r, int c) {
		for (int i = 0; i < R; i++) {
			for (int j = 0; j < C; j++) {
				if (R - i < r) {
					System.out.println("NO");
					return;
				}
				if (G[i][j] == P[0][0]) {
					//Scan further
					if (compareArray(G, P, r, c, R, C, i, j) == 1) {
						System.out.println("YES");
						return;
					}
				}
			}
		}
		System.out.println("NO");
	}
	
	static int compareArray (int[][] G, int[][] P, int r, int c, int R, int C, int ith, int jth) {
		int tempR = ith;
		int tempC = jth;
		for (int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++) {
				if (P[i][j] != G[tempR][tempC]) {
					return 0;
				}
				tempC++;
				if (tempC >= C) {
					return 0;
				}
			}
			tempR++;
			tempC = jth;
		}
		return 1;
	}

}
