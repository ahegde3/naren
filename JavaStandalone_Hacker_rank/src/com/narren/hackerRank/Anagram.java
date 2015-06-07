package com.narren.hackerRank;

import java.util.Scanner;

public class Anagram {

	public static void main(String[] args) {
		//System.out.println(process("xyyx"));
		Scanner in = new Scanner(System.in);
		int T = in.nextInt();
		for (int i = 0; i < T; i++) {
			String input = in.next();
			System.out.println(process(input));
		}
	}
	private static int process (String input) {
		int result = 0;
		if (input.length() % 2 != 0) {
			return -1;
		}
		String firstStr = input.substring(0, (input.length()/2));
		String secStr = input.substring((input.length()/2), input.length());
		int array[] = new int[123];
		for (int i = 0; i < secStr.length(); i++) {
			array[secStr.charAt(i)] ++;
		}
		for (int i = 0; i < firstStr.length(); i++) {
			if (array[firstStr.charAt(i)] > 0) {
				array[firstStr.charAt(i)] -- ;
			} else {
				result++;
			}
		}
		return result;
	}
}
