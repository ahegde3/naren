package com.narren.leetCode;

/**
 * 
Implement atoi to convert a string to an integer.

Hint: Carefully consider all possible input cases.
If you want a challenge, please do not see below and ask yourself what are the possible input cases.

Notes: It is intended for this problem to be specified
vaguely (ie, no given input specs). You are responsible to gather all the input requirements up front.

https://leetcode.com/problems/string-to-integer-atoi/
 * 
 * @author nsbisht
 *
 */
public class StringToInteger {

	public static void main(String[] args) {
		System.out.println(myAtoi("7896"));;
	}
	public static int myAtoi(String str) {
		char[] chars = str.toCharArray();
		int pow = chars.length - 1;
		int ret = 0;
		for(int i = 0; i < chars.length; i++) {
			int n = chars[i] - '0';
			ret += Math.pow(10, pow) * n;
			pow--;
		}
		return ret;
	}

}
