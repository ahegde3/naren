package com.narren.hackerRank;

import java.util.Scanner;

public class SecurePasswordSystem {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int T = in.nextInt();
		while (T > 0) {
			int m = in.nextInt();
			int M = in.nextInt();
			if (secureEnough(m, M)) {
				System.out.println("YES");
			} else {
				System.out.println("NO");
			}
			T--;
		}
	}
	static boolean secureEnough(int m, int M) {
		long security = 0;
		while (m <= M) {
			security += Math.pow(10, m);
			if (security > 1000000) {
				return true;
			}
			m++;
		}
		return false;
	}
}
