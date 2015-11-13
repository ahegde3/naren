package com.narren.coding.interview;

public class ClosestIntegerSameWeight {

	public static void main(String[] args) {
		System.out.println(closestInt(8));
	}
	private static int closestInt(int n) {
		int temp = n;
		int retVal = 0;
		int counter = 0;
		while(n > 0) {
			if ((n & 1) != ((n >> 1) & 1)) {
				break;
			}
			counter++;
			n >>= counter;
		}
		retVal = swapBits(temp, counter, counter + 1);
		return retVal;
	}
	
	private static int swapBits(int n, int i, int j) {
		int bitmask = 1 << i | 1 << j;
		return n ^= bitmask;
	}
}
