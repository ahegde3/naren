package com.narren.coding.interview;

public class SwapBits {

	public static void main(String[] args) {
		swap(13, 4, 2);
	}
	private static void swap(long l, int i, int j) {
		long iv = (l >> i) & 1;
		long jv = (l >> j) & 1;
		if (jv != iv) {
			long bitMask = (1 << i) | (1 << j);
			System.out.println(bitMask);
			l ^= bitMask;
		}
		
		System.out.println(l);
	}
}
