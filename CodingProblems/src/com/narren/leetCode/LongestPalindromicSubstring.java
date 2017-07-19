package com.narren.leetCode;
/**
Given a string s, find the longest palindromic substring in s. You may assume that the maximum length of s is 1000.

Example:

Input: "babad"

Output: "bab"

Note: "aba" is also a valid answer.
Example:

Input: "cbbd"

Output: "bb"
 * 
 * @author nsbisht
 *
 */
public class LongestPalindromicSubstring {
	
	public static void main(String[] args) {
		LongestPalindromicSubstring lps = new LongestPalindromicSubstring();
		System.out.println(lps.longestPalindrome("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabcaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
	}
	public String longestPalindrome(String s) {
		char[] chars = s.toCharArray();
		boolean[][] table = new boolean[chars.length][chars.length];
		int longestPalindrome = 0;
		int longestPalindromeStart = -1;
		String longestPalindromeStr = "";
		
		for(int i = 0; i < chars.length; i++) {
			table[i][i] = true;
			if(1 > longestPalindrome) {
				longestPalindrome = 1;
				longestPalindromeStart = i;
			}
		}
		for(int i = 0; i < chars.length - 1; i++) {
			if(chars[i] == chars[i + 1]) {
				table[i][i + 1] = true;
				if((i + 1 - i + 1) > longestPalindrome) {
					longestPalindrome = 2;
					longestPalindromeStart = i;
				}	
			}
			
		}
		
		for(int k = 2; k < chars.length; k++) {
			for(int i = 0; i + k < chars.length; i++) {
				int j = i + k;
				if(chars[i] == chars[j] && table[i + 1][j - 1]) {
					table[i][j] = true;
					if((j - i + 1) > longestPalindrome) {
						longestPalindrome = j - i + 1;
						longestPalindromeStart = i;
					}	
				}
			}
		}
		for(int i = longestPalindromeStart; i < longestPalindromeStart + longestPalindrome; i++) {
			longestPalindromeStr += chars[i];
		}
		return longestPalindromeStr;
	}


}
