package com.narren.hackerRank;

import java.util.Scanner;

public class SherlockandPairs {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int T = in.nextInt();
		while (T > 0) {
			int N = in.nextInt();
			double[] bigArr = new double[1000001];
			double[] duplicates = new double[N];
			int counter = 0;
			for (int i = 0; i < N; i++) {
				//arr[i] = in.nextInt();
				int element = in.nextInt();
				if (bigArr[element] == 0) {
					bigArr[element] = 1;
				} else {
					//duplicate
					if (bigArr[element] == 1) {
						duplicates[counter] = element;
						counter++;
					}
					bigArr[element] = bigArr[element] + 1;
				}
			}
			findDuplicates(bigArr, duplicates);
			T--;
		}
	}

//	static void findDuplicates(int[] input) {
//		for (int i = 0; i < input.length; i++) {
//			if (input[Math.abs(input[i])] >= 0) {
//				input[Math.abs(input[i])] = -input[Math.abs(input[i])];
//			}
//			else {
//				//Duplicate
//				System.out.println(Math.abs(input[i]));
//			}
//		}
//	}
	static void findDuplicates(double[] input, double[] dupArry) {
		double finalRes = 0;
		for (int i = 0; i < dupArry.length; i++) {
			if (dupArry[i] == 0) {
				break;
			}
			double n = input[(int) dupArry[i]];
			finalRes += fact(n);
			
		}
		String res = "" + finalRes;
		String[] fRes = res.split("\\.");
		System.out.println(fRes[0]);
	}
	static double fact (double n) {
		return n * (n-1);
//		if (n > 1) {
//			return n*fact(n-1);
//		} else {
//			return 1;
//		}
	}
}
