package com.narren.hackerRank;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class SherlockandSquares {
	static BufferedReader in = new BufferedReader(new InputStreamReader(
			System.in));
	static StringBuilder out = new StringBuilder();
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		for (int i = 0; i < T; i++) {
			int A = Integer.parseInt(sc.next());
			int B = Integer.parseInt(sc.next());
			int count = 0;
			for (int j = A; j <= B; ) {
				double d = Math.sqrt(j); 
				if (d == (int)d) {
					j = (int) Math.pow((d + 1), 2);
					count++;
					continue;
				}
				j++;
			}
			System.out.println(count);
		}
	}
}
