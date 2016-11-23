package com.narren.leetCode;

/**
 * 
https://leetcode.com/problems/wildcard-matching/
 * 
Implement wildcard pattern matching with support for '?' and '*'.

'?' Matches any single character.
'*' Matches any sequence of characters (including the empty sequence).

The matching should cover the entire input string (not partial).

The function prototype should be:
bool isMatch(const char *s, const char *p)

Some examples:
isMatch("aa","a") → false
isMatch("aa","aa") → true
isMatch("aaa","aa") → false
isMatch("aa", "*") → true
isMatch("aa", "a*") → true
isMatch("ab", "?*") → true
isMatch("aab", "c*a*b") → false
 * 
 * @author ns.bisht
 *
 */
public class WildcardMatching {

	 public boolean isMatch(String s, String p) {
	        char[] sc = s.toCharArray();
	        char[] pc = p.toCharArray();
	        int index = 0;
	        for(int i = 0; i < sc.length; i++) {
	        	if(sc[i] == pc[index] || pc[index] == '?') {
	        		index++;
	        		continue;
	        	}
	        	if(pc[index] == '*') {
	        		int tempIndex = index;
	        		while(pc[tempIndex]) {
	        			
	        		}
	        	}
	        	
	        }
	    }
}
