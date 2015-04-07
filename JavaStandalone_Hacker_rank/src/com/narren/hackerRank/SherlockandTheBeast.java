package com.narren.hackerRank;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class SherlockandTheBeast {
	static BufferedReader in = new BufferedReader(new InputStreamReader(
			System.in));
	static StringBuilder out = new StringBuilder();

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		for (int i = 0; i < T; i++) {
			int N = sc.nextInt();
			if (N < 3) {
				System.out.println("-1");
				continue;
			}
			int fives = N/3;
			int threes = N%3;
			if (threes == 0) {
				// All fives
				//System.out.println(fives);
				StringBuilder str = new StringBuilder();
				for (int j = 0; j < fives*3; j++) {
					str.append("5");
				}
				System.out.println(str);
				continue;
			}
			int R = 5 - threes;
			int L = 3*fives - R;
			if (L%3 == 0) {
				int finalF = L;
				int finalT = N - L;
				//System.out.println(finalF + " " + finalT);
				StringBuilder str = new StringBuilder();
				for (int j = 0; j < finalF; j++) {
					str.append("5");
				}
				for (int j = 0; j < finalT; j++) {
					str.append("3");
				}
				System.out.println(str);
			} else {
				int secThree = N/5;
				if ((N%5) == 0) {
					// All threes
					//System.out.println(fives);
					StringBuilder str = new StringBuilder();
					for (int j = 0; j < (secThree)*5; j++) {
						str.append("3");
					}
					System.out.println(str);
					continue;
				}
				else {
					System.out.println("-1");
				}

			}


		}
	}
}
