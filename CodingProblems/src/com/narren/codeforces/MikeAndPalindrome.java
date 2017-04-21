package com.narren.codeforces;

import java.util.Scanner;

/**
A. Mike and palindrome
time limit per test2 seconds
memory limit per test256 megabytes
inputstandard input
outputstandard output

Mike has a string s consisting of only lowercase English letters. He wants to change exactly one character from the string so that the resulting one is a palindrome.

A palindrome is a string that reads the same backward as forward, for example strings "z", "aaa", "aba", "abccba" are palindromes, but strings "codeforces", "reality", "ab" are not.

Input
The first and single line contains string s (1 ≤ |s| ≤ 15).

Output
Print "YES" (without quotes) if Mike can change exactly one character so that the resulting string is palindrome or "NO" (without quotes) otherwise.
input
abccaa
output
YES
input
abbcca
output
NO
input
abcda
output
YES

http://codeforces.com/contest/798/problem/0
 * 
 * @author nsbisht
 *
 */
public class MikeAndPalindrome {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
        String s = sc.next();
        String res = canDoPalin(s) ? "YES" : "NO";
        System.out.println(res);
	}
	static boolean canDoPalin(String s) {
		boolean onceReplace = false;
		char[] in = s.toCharArray();
		for(int i = 0, j = in.length - 1; i < j; i++, j--) {
			if(in[i] != in[j]) {
				if(onceReplace) {
					return false;
				}
				onceReplace = true;
			}
		}
		return true;
	}
}
