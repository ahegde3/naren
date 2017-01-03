package com.narren.leetCode;

/**
 * 
Given an array S of n integers, find three integers in S such that the sum is closest to a given number, target. Return the sum of the three integers. You may assume that each input would have exactly one solution.

    For example, given array S = {-1 2 1 -4}, and target = 1.

    The sum that is closest to the target is 2. (-1 + 2 + 1 = 2).

    https://leetcode.com/problems/3sum-closest/
 * 
 * @author nsbisht
 *
 */
public class SumClosest3 {
	
	public int threeSumClosest(int[] nums, int target) {
		int res = 0;
		int min = Integer.MAX_VALUE;
		for(int i = 0; i < nums.length - 2; i++) {
			for(int j = i + 1; j < nums.length - 1; j++) 
			for(int k = j + 1; k < nums.length; k++) {
				int sum = nums[i] + nums[j] + nums[k];
				int tempSum = sum > target ? sum - target : target - sum;
				min = Math.min(min, tempSum);
				if(min == tempSum) {
					res = sum;
				}
				if(res == target) {
					return target;
				}
			}
		}
		return res;
	}
	
	public static void main(String[] args) {
		SumClosest3 obj = new SumClosest3();
		System.out.println(obj.threeSumClosest(new int[]{1,}, 10));
	}
}
