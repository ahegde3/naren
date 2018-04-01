package com.narren.leetCode;

public class MaximumSubArray {

	public static void main(String[] args) {
		MaximumSubArray msa = new MaximumSubArray();
		System.out.println(msa.getMaxSubArray(new int[]{-2,-3,4,-1,-2,1,5,-3}));
	}
	
	int getMaxSubArray(int[] nums) {
		return maxSubArray(nums, 0, nums.length - 1);
	}
	
	int maxSubArray(int[] nums, int l, int h) {
		if(l == h) {
			return nums[l];
		}
		
		int mid = (h - l) / 2 + l;
		int left = maxSubArray(nums, l, mid);
		int right = maxSubArray(nums, mid + 1, h);
		int crossMax = maxCrossingSubArray(nums, l, h, mid);
		
		return Math.max(left, Math.max(right, crossMax)); 
	}
	
	int maxCrossingSubArray(int[] nums, int l, int h, int m) {
		
		int leftMax = Integer.MIN_VALUE;
		int sum = 0;
		
		for(int i = m; i >= l; i--) {
			sum += nums[i];
			if(sum > leftMax) {
				leftMax = sum;
			}
		}
		
		int rightMax = Integer.MIN_VALUE;
		sum = 0;
		
		for(int i = m + 1; i <= h; i++) {
			sum += nums[i];
			if(sum > rightMax) {
				rightMax = sum;
			}
		}
		
		return leftMax + rightMax;
		
	}
}
