package com.narren.leetCode;

public class MaximumProductSubarray {
	public int maxProduct(int[] nums) {
		int mainMax = Integer.MIN_VALUE;
		int curMax = Integer.MIN_VALUE;

		for(int i : nums) {
			if(curMax == Integer.MIN_VALUE) {
				curMax = i;
			} else {
				int temp = Math.max(curMax, Math.max(curMax * i, i));
				if(temp == curMax) {
					curMax = i;
				} else {
					curMax = temp;
				}
			}
			mainMax = Math.max(mainMax, curMax);
		}

		return mainMax;        
	}

	public static void main(String[] args) {
		new MaximumProductSubarray().maxProduct(new int[]{-2, 3, -4});
	}
}
