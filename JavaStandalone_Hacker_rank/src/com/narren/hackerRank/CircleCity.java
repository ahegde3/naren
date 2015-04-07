package com.narren.hackerRank;

import java.util.Scanner;

public class CircleCity {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int T = in.nextInt();
		while (T > 0) {
			int D = in.nextInt();
			int K = in.nextInt();
			int res = process(D);
			String result = (res <= K) ? "possible" : "impossible";
			System.out.println(result);
			T--;
		}
	}
	
	static int process (double d) {
		double sqrt = Math.sqrt(d);
		int res = 4; // by default
		for (double i = 1; i <= (sqrt - 1); i++) {
			double sum = Math.sqrt(d - Math.pow(i, 2));
			if (sum == (int)sum) {
				res += 4;
			}
		}
		//System.out.println(res);
		return res;
	}
}
