package com.narren.hackerMeter;

import java.util.Arrays;
import java.util.Scanner;

public class Palindrome {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
	    int cases = Integer.parseInt(scanner.nextLine());
	    for(int i = 0; i < cases; i++) {
	      run(scanner);
	    }
	}
	public static void run(Scanner scanner) {
	    String str = scanner.next();
	    String reverse = compute(str, str.length());
	    String result = (str.equals(reverse.toString())) ? "Y" : "N" ;
	    System.out.println(result);
	  }
	
	private static String compute(String str, int length) {
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
}
