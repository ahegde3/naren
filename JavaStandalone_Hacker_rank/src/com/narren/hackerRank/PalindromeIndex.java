package com.narren.hackerRank;

import java.util.Scanner;

public class PalindromeIndex {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int T = in.nextInt();
		for (int i = 0; i < T; i++) {
			String input = in.next();
			System.out.println(process(input));
		}
	}
	
	private static String isPalindrome(String str, int length) {

		int n = length - 1;
		char []strArray = str.toCharArray();
		for (int j = (n - 1) >> 1; j >= 0; --j) {
			char temp = strArray[j];
			char temp2 = strArray[n - j];
			strArray[j] = temp2;
			strArray[n - j] = temp;
		}
		return String.valueOf(strArray);
	
	}
	private static int process (String str) {
		int array[] = new int[str.length()];
		int index = -1;
		int temps;
		int tempe = 0;
		boolean found = false;
		if (str.length() < 2) {
			return -1;
		}
		for (int j = 0, i = str.length()-1; j < i; ) {
			//array[j] = str.charAt(j);
			if (str.charAt(j) == str.charAt(i) && !found) {
				j++;
				i--;
				continue;
			} else {
				String tempStr = str.substring(j, i);
				String reverse = isPalindrome(tempStr, tempStr.length());
				if (tempStr.equals(reverse)) {
					return i;
				} else {
					return j;
				}
				/**
				 * 

				if (!found) {
					if (j == i-1) {
						index = j;
						return index;
					}
					temps = j;
					tempe = i;
					i--;
					found = true;
				} else {
					if (str.charAt(j) == str.charAt(i)) {
						index = tempe;
						return index;
					} else {
						index = j;
						return index;
					}
					
				}				 */
			}
		}
		return index;
	}
}
