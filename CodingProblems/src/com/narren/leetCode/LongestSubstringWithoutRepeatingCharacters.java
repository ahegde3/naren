package com.narren.leetCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Given a string, find the length of the longest substring without repeating characters.

Examples:

Given "abcabcbb", the answer is "abc", which the length is 3.

Given "bbbbb", the answer is "b", with the length of 1.

Given "pwwkew", the answer is "wke", with the length of 3. Note that the answer must be a substring, "pwke" is a subsequence and not a substring.


 * @author naren
 *
 */
public class LongestSubstringWithoutRepeatingCharacters {
	public int lengthOfLongestSubstring(String s) {
		if(s == null || s.isEmpty()) {
			return 0;
		}
		char[] c = s.toCharArray();
		int maxLen = 1;
		Map<Character, Integer> chars;

		for(int i = 0; i < c.length; i++) {
			chars = new HashMap<Character, Integer>();
			chars.put(c[i], 1);
			for(int j = i + 1; j < c.length; j++) {
				if(chars.containsKey(c[j])) {
					maxLen = Math.max(maxLen, j - i);
					break;
				}
				if(j == c.length - 1) {
					return Math.max(maxLen,j - i + 1);
				}
				chars.put(c[j], 1);
			}
		}

		return maxLen;
	}
}
