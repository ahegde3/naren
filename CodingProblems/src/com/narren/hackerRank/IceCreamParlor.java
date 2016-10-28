package com.narren.hackerRank;

import java.util.Scanner;

public class IceCreamParlor {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int T = in.nextInt();
		while (T > 0) {
			int M = in.nextInt();
			int N = in.nextInt();
			String[] mainArr = new String[N+1];
			int [] inArr = new int [N+1];
			for (int i = 1; i <= N; i++) {
				int Ci = in.nextInt();
				inArr[i] = Ci;
			}
			process(inArr, M);
			T--;
		}
	}

	static void process (int[] input, int M) {
		for (int i = 1; i < input.length; i++) {
			for (int j = i + 1; j < input.length; j++) {
				if (input[i] + input[j] == M) {
					System.out.println(i + " " + j);
					return;
				}
			}
		}
	}
}
