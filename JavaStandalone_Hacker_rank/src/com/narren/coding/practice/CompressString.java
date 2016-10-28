package com.narren.coding.practice;

public class CompressString {

	public static void main(String[] args) {
		String str = "aaabbcc";
		System.out.println(checkCompress(str));
	}

	private static String checkCompress(String str) {
		if (str == null || str.isEmpty() || str.length() < 3) {
			return str;
		}
		int count = 0;
		StringBuilder finalStr = new StringBuilder();
		for (int i = 0; i < str.length() - 1; i++) {
			count++;
			if (i + 1 >= str.length() || str.charAt(i) != str.charAt(i + 1)) {
				finalStr.append(str.charAt(i)).append(count);
				count = 0;
			}
		}
		String compressedStr = finalStr.toString();
		return compressedStr.length() < str.length() ? compressedStr : str;
	}

}
