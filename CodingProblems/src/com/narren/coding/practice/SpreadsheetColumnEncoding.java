package com.narren.coding.practice;

public class SpreadsheetColumnEncoding {

	public static void main(String[] args) {
		System.out.println(encode("ZZ"));
	}
	private static int encode(String in) {
		int result = 0;
		int j = 0;
		for (int i = in.length() - 1; i >= 0; i--) {
			result += Math.pow(26, j) * ((in.charAt(i) - 'A') + 1);
			j++;
		}
		
		return result;
	}
}
