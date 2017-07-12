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
	
	public static void main(String[] args) {
		LongestSubstringWithoutRepeatingCharacters lcr = new LongestSubstringWithoutRepeatingCharacters();
		System.out.println(lcr.lengthOfLongestSubstring("aab"));
	}
	public int lengthOfLongestSubstring(String str) {
		int s = 0;
		int e = 0;
		if(str == null || str.isEmpty()) {
			return 0;
		}
		char[] charArray = str.toCharArray();
		int maxLen = 1;
		int maxChar = 1;
		Map<Character, Integer> chars = new HashMap<Character, Integer>();
		for(char c : charArray) {
			if(chars.containsKey(c)) {
				chars.put(c, chars.getOrDefault(c, 0) + 1);
				maxChar = Math.max(maxChar, chars.get(c));
				maxLen = Math.max(maxLen, e - s);
				s++;
				while(chars.get(charArray[s]) > 1 && s < e) {
					chars.put(c, chars.get(c) - 1);
					maxChar -= 1;
					s++;
				}
				e++;
			} else {
				chars.put(c, chars.getOrDefault(c, 0) + 1);
				e++;
			}
		}
		if(maxChar < 2) {
			maxLen = Math.max(maxLen, e - s);	
		}

		return maxLen;
	}
}
