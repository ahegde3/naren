package com.narren.hackerRank;

import java.util.Scanner;

public class PlusMinus {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		float N = sc.nextInt();
		float neg = 0;
		float pos = 0;
		float zero = 0;
		for (int i = 0; i < N; i++) {
			int num = sc.nextInt();
			if (num > 0) {
				pos++;
			} else if (num < 0) {
				neg++;
			} else {
				zero++;
			}
		}
		System.out.println((long)pos/N);
		System.out.println((long)neg/N);
		System.out.println((long)zero/N);
	}

}
