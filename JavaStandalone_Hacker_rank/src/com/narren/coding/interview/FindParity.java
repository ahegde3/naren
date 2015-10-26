package com.narren.coding.interview;

public class FindParity {

	public static void main(String[] args) {
		System.out.println(parity(3));
	}
	private static int parity(long l) {
		int result = 0;
		while (l > 0) {
			result += (l & 1);
			l >>= 1;
		}
		int returnVal = (result % 2 == 0) ? 0 : 1;
		return returnVal;
	}
}
