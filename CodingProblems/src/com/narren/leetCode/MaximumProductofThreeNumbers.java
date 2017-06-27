package com.narren.leetCode;

import java.util.Arrays;

/**
 * Given an integer array, find three numbers whose product is maximum and output the maximum product.

Example 1:
Input: [1,2,3]
Output: 6
Example 2:
Input: [1,2,3,4]
Output: 24
Note:
The length of the given array will be in range [3,104] and all elements are in the range [-1000, 1000].
Multiplication of any three numbers in the input won't exceed the range of 32-bit signed integer.
 * @author naren
 *
 */
public class MaximumProductofThreeNumbers {
	
	public int maximumProduct(int[] nums) {
			
		if(nums.length == 3) {
			return nums[0] * nums[1] * nums[2];
		}
		
		if(nums.length < 6) {
			int tres = Integer.MIN_VALUE;
			for(int i = 0; i < nums.length; i++) {
				for(int j = i + 1; j < nums.length; j++) {
					for(int k = j + 1; k < nums.length; k++) {
						tres = Math.max(tres, nums[i] * nums[j] * nums[k]);
					}
				}
			}
			return tres;
		} else {
			Arrays.sort(nums);
			
			int n1 = nums[0] * nums[1] * nums[nums.length - 1];
			int n2 = nums[nums.length - 3] * nums[nums.length - 2] * nums[nums.length - 1];
			return Math.max(n1, n2);
		}
	}
}
