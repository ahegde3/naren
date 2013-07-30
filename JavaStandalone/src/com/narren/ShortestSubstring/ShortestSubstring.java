package com.narren.ShortestSubstring;

public class ShortestSubstring {

	public static void main(String[] args) {
		String str1 = "zebra";
		String str2 = "bras";
		String str3 = "ebbs";
		System.out.println(str1.replaceAll("[" + str2 + "]", "").replaceAll("[" + str3 + "]", ""));
		System.out.println(str2.replaceAll("[" + str3 + "]", "").replaceAll("[" + str1 + "]", ""));
		System.out.println(str3.replaceAll("[" + str2 + "]", "").replaceAll("[" + str1 + "]", ""));
	}
}
