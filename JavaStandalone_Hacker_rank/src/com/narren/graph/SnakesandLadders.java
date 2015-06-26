package com.narren.graph;

import java.util.Scanner;

public class SnakesandLadders {
	public static void main(String[] args) {
		/**
		1
		3
		6 80
		8 55
		13 99
		1
		56 12
		 */
		Scanner in = new Scanner(System.in);
		int T = in.nextInt();
		while (T > 0) {
			int L = in.nextInt();
			while (L > 0) {
				int start = in.nextInt();
				int end = in.nextInt();	
				L--;
			}
			int S = in.nextInt();
			while (S > 0) {
				int start = in.nextInt();
				int end = in.nextInt();	
				T--;
			}
		}
	}

}
