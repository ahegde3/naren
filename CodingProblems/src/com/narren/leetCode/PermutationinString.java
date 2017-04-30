package com.narren.leetCode;
/**
 * 
Given two strings s1 and s2, write a function to return true if s2 contains the permutation of s1. In other words, one of the first string's permutations is the substring of the second string.

Example 1:
Input:s1 = "ab" s2 = "eidbaooo"
Output:True
Explanation: s2 contains one permutation of s1 ("ba").
Example 2:
Input:s1= "ab" s2 = "eidboaoo"
Output: False
Note:
The input strings only contain lower case letters.
The length of both given strings is in range [1, 10,000].

https://leetcode.com/contest/leetcode-weekly-contest-30/problems/permutation-in-string/

 * 
 * @author naren
 *
 */
public class PermutationinString {

	public static void main(String[] args) {
		PermutationinString ps = new PermutationinString();
		System.out.println(ps.checkInclusion("", ""));
	}
	public boolean checkInclusion(String s1, String s2) {
		if(s1.equals(s2)) {
			return true;
		}
		if(s1.length() > s2.length()) {
			return false;
		}
		char[] c1 = s1.toCharArray();
		char[] c2 = s2.toCharArray();

		int[] a1 = new int[124];
		int[] a2 = new int[124];

		for(char c : c1) {
			a1[c]++;
		}

		for(int i = 0; i < c2.length ; i++) {
			for(int j = i, l = 0 ; l < c1.length && j < c2.length; j++, l++) {
				a2[c2[j]]++;
			}
			boolean nfound = true;
			for(int k = 97; k < 123; k++) {
				if(a1[k] > 0) {
					if(a1[k] != a2[k]) {
						a2 = new int[124];
						nfound = false;
						break;
					}
				}
				if(a2[k] > 0 && a1[k] <= 0) {
					a2 = new int[124];
					nfound = false;
					break;
				}
			}
			if(nfound) {
				return true;				
			}
		}

		return false;
	}
}
