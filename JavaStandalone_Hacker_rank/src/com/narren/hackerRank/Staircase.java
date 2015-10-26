package com.narren.hackerRank;

import java.util.Scanner;

public class Staircase {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		for (int i = 1; i <= N; i++) {
			int temp = i;
			while (N - temp > 0) {
				System.out.print(" ");
				temp++;
			}
			temp = 1;
			while (temp <= i) {
				System.out.print("#");
				temp++;
			}
			System.out.println();
		}
	}

}
