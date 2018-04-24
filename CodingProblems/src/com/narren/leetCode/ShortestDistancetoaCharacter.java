package com.narren.leetCode;
/**
 * 
Given a string S and a character C, return an array of integers representing the shortest distance from the character C in the string.

Example 1:

Input: S = "loveleetcode", C = 'e'
Output: [3, 2, 1, 0, 1, 0, 0, 1, 2, 2, 1, 0]
 

Note:

S string length is in [1, 10000].
C is a single character, and guaranteed to be in string S.
All letters in S and C are lowercase.

 * @author naren
 *
 */
public class ShortestDistancetoaCharacter {
	static final int MAX_VALUE = 50000;
    public int[] shortestToChar(String S, char C) {
    	
        int[] dis = new int[S.length()];
        char[] input = S.toCharArray();
        
        int prev = -MAX_VALUE;

        for(int i = 0; i < dis.length; i++) {
        	if(input[i] == C) {
        		prev = i;
        	}
        	dis[i] = i - prev;
        }
        
        prev = MAX_VALUE;

        for(int i = dis.length - 1; i >= 0; i--) {
        	if(input[i] == C) {
        		prev = i;
        	}
        	dis[i] = Math.min(dis[i], prev - i);
        }
        
        return dis;
        
    }
    
}
