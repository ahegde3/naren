package com.narren.leetCode;
/**
Given a string s, find the longest palindromic substring in s. You may assume that the maximum length of s is 1000.

Example 1:

Input: "babad"
Output: "bab"
Note: "aba" is also a valid answer.

Example 2:

Input: "cbbd"
Output: "bb"


 * 
 * @author nsbisht
 *
 */
public class LongestPalindromicSubstring_2 {
	boolean isPalindrome(char[] in, int i, int j) {
		while(i < j) {
			if(in[i] != in[j]) {
				return false;
			}
			i++;
			j--;
		}
		return true;
	}

	public String longestPalindrome(String s) {
		if(s == null || s.length() < 1) {
			return "";
		}

		int[][] prev = new int[123][s.length()];
		for(int i = 0; i < 123; i++) {
			for(int j = 0; j < s.length(); j++) {
				prev[i][j] = -1;
			}
		}

		int maxPalinLength = 0;
		String palindrome = "";

		char[] input = s.toCharArray();
		
		for(int i = 0; i < input.length; i++) {
			if(prev[input[i]][0] == -1) {
				prev[input[i]][0] = i;
			} else {
				int j = 0;
				while(prev[input[i]][j] != -1) {
					if(i - prev[input[i]][j] > maxPalinLength) {
						if(isPalindrome(input, prev[input[i]][j], i)) {
							maxPalinLength = i - prev[input[i]][j];
							palindrome = s.substring(prev[input[i]][j], i + 1);
						}
					}
					j++;
				}
				prev[input[i]][j] = i;
			}
		}
		return palindrome == "" ? "" + input[0] : palindrome;
	}
	
	public static void main(String[] args) {
		LongestPalindromicSubstring_2 lps = new LongestPalindromicSubstring_2();
		String res = lps.longestPalindrome("cbba");
		System.out.println(res);
	}
}
