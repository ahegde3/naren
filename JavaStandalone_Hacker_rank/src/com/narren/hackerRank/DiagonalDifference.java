package com.narren.hackerRank;

import java.util.Scanner;

public class DiagonalDifference {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int[][] arr = new int[N][N];
		int sumFirstD = 0;
		int sumSecondD = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				arr[i][j] = sc.nextInt();
				if (i == j) {
					sumFirstD += arr[i][j];
				}
			}
		}

		for (int i = 0, j = N - 1; i < N && j >= 0; i++, j--) {
			sumSecondD += arr[i][j];
		}

		System.out.println(Math.abs(sumFirstD - sumSecondD));
	}

}