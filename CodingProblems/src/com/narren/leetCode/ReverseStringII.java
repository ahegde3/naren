package com.narren.leetCode;
/**
 * 
Given a string and an integer k, you need to reverse the first k characters for every 2k
characters counting from the start of the string. If there are less than k characters left,
reverse all of them. If there are less than 2k but greater than or equal to k characters, then reverse the first k characters and left the other as original.
Example:
Input: s = "abcdefg", k = 2
Output: "bacdfeg"
Restrictions:
The string consists of lower English letters only.
Length of the given string and k will in the range [1, 10000]
 * 
 * @author naren
 *
 */
public class ReverseStringII {
	public static void main(String[] args) {
		System.out.println(new ReverseStringII().reverseStr("abcdefg", 2));
	}
	public String reverseStr(String s, int k) {
		if(k < 1) {
			return s;
		}
		for(int i = 0; i < s.length();) {
			s = reverse(s, i, k);
			i += 2 * k;
		}
		return s;
	}

	String reverse(String str, int s, int len) {
		int e = (s + len) - 1;
		char[] input = str.toCharArray();
		if(e >= str.length()) {
			e = str.length() - 1;
		}
		while(e > s) {
			char temp = input[s];
			input[s] = input[e];
			input[e] = temp;
			s++;
			e--;
		}
		return new String(input);
	}
}
