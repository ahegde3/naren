package com.narren.coding.practice;

public class ReverseString {

	public static void main(String[] args) {
		System.out.println(reverseString("aaaa"));
		
	}
	
	static String reverseString(String input) {
		char[] in = input.toCharArray();
		for(int i = 0, j = in.length  - 1; i < j; i++, j--) {
			char temp = in[i];
			in[i] = in[j];
			in[j] = temp;
		}
		return new String(in);
	}
}
