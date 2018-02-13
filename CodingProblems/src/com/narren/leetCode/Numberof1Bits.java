package com.narren.leetCode;

public class Numberof1Bits {
	public int hammingWeight(int n) {
        int ones = 0;
        int mask = 1;
        for(int i = 0; i < 32; i++) {
        	if((mask & n) == mask) {
        		ones++;
        	}
        	mask <<= 1;
        }
        
        return ones;
    }
	
	public static void main(String[] args) {
		System.out.println(new Numberof1Bits().hammingWeight(500));
	}
}
