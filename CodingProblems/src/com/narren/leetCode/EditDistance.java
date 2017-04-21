package com.narren.leetCode;
/**
 * 
 * Given two words word1 and word2, find the minimum number of steps required to convert word1 to word2. (each operation is counted as 1 step.)

You have the following 3 operations permitted on a word:

a) Insert a character
b) Delete a character
c) Replace a character

https://leetcode.com/problems/edit-distance/#/description
 * 
 * @author naren
 *
 */
public class EditDistance {
	public int minDistance(String word1, String word2) {
		char[] source = word1.toCharArray();
		char[] destination = word2.toCharArray();
		int s = source.length;
		int d = destination.length;
		int[][] dp = new int[s + 1][d + 1];
		
		for(int i = 0; i <= s; i++) {
			dp[i][0] = i;
		}
		
		for(int i = 0; i <= d; i++) {
			dp[0][i] = i;
		}
		
		for(int i = 1; i <= s; i++) {
			for(int j = 1; j <= d; j++) {
				if(source[i][j] == destination[i][j]) {
					
				}
			}
		}
	}
}
