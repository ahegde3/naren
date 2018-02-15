package com.narren.leetCode;

public class HammingDistance {

	public int hammingDistance(int x, int y) {

		int dis = 0;
		int i = 0;

		while(i < 32) {
			dis += ((x >> i) & 1) ^ ((y >> i) & 1);
			i++;
		}

		return dis;

	}
	
	public static void main(String[] args) {
		System.out.println(new HammingDistance().hammingDistance(11, 4));
	}
}
