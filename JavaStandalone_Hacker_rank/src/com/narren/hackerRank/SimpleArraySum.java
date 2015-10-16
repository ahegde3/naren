package com.narren.hackerRank;

import java.util.Scanner;

public class SimpleArraySum {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int sum = 0;
		while (N > 0) {
			sum += sc.nextInt();
			N--;
		}
		System.out.println(sum);
	}
}
