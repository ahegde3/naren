package com.narren.hackerRank;

import java.util.Scanner;

public class TwoStrings {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int T = in.nextInt();
		for (int i = 0; i < T; i++) {
			String input1 = in.next();
			String input2 = in.next();
			int array[] = new int[123];
			for (int j = 0; j < input2.length(); j++) {
				array[input2.charAt(j)] ++;
			}
			System.out.println(process(input1, array));
		}
	}
	private static String process (String input, int[] array) {
		String result = "NO";
		for (int i = 0; i < input.length(); i++) {
			if (array[input.charAt(i)] > 0) {
				return "YES" ;
			} 
		}
		return result;
	}


}
