package com.narren.coding.practice;

public class PermutationOfString {

	
	static void print(char[] c, int l, int r) {
		if(l == r) {
			System.out.println(c);
		} else {
		for(int i = l; i <= r; i++) {
			swap(c, l, i);
			print(c, l + 1, r);
			swap(c, l, i);
		}
		}
	}
	
	private static void swap(char[] c, int i, int j) {
		char temp = c[i];
		c[i] = c[j];
		c[j] = temp;
	}
	
	public static void main(String[] args) {
		String s = "abcd";
		print(s.toCharArray(), 0, 3);
	}
}
