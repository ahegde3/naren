package com.narren.coding.interview;

public class StringToInt {

	public static void main(String[] args) {
		System.out.println(toInt("-234"));
	}
	public static int toInt(String str) {
		boolean isNegative = false;
		int result = 0;
		int i = 0;
		if (str.length() > 0) {
			if (str.charAt(0) == '-') {
				isNegative = true;
				i++;
			}
		}
		while (i < str.length())  {
			int c = str.charAt(i);
			//System.out.println((c - '0'));
			result = result * 10 + (c - '0');
			i++;
		}
		
		return isNegative ? -result : result;
		
	}
}
