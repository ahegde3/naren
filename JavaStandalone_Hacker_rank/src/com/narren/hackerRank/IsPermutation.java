package com.narren.hackerRank;

public class IsPermutation {

	public static void main(String[] args) {
		System.out.println(isPermutation("a", ""));
	}
	private static boolean isPermutation (String s1, String s2) {
		if (s1.length() != s2.length()) {
			return false;
		}
		int[] arr = new int[123]; 
		char[] charArr1 = s1.toCharArray();
		for (int i = 0; i < s1.length(); i++) {
			arr[charArr1[i]]++;
		}
		
		char[] charArr2 = s2.toCharArray();
		
		for (int i = 0; i < s2.length(); i++) {
			if (arr[charArr2[i]] > 0) {
				arr[charArr2[i]]--;
			} else {
				return false;
			}
		}
		return true;
	}
}
