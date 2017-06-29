package com.narren.leetCode;

import java.util.HashMap;
import java.util.Map;

/**
 * 
Given an array of integers, return indices of the two numbers such that they add up to a specific target.

You may assume that each input would have exactly one solution, and you may not use the same element twice.

Example:
Given nums = [2, 7, 11, 15], target = 9,

Because nums[0] + nums[1] = 2 + 7 = 9,
return [0, 1].
 * 
 * @author naren
 *
 */
public class TwoSum {
	public int[] twoSum(int[] nums, int target) {
		int[] res = new int[2];
		Map<Integer, Integer> elements = new HashMap<Integer, Integer>();

		for(int i = 0; i < nums.length; i++) {
			if(elements.containsKey(nums[i])) {
				res[0] = elements.get(nums[i]);
				res[1] = i;
				return res;
			}
			elements.put(target - nums[i], i);
		}

		return res;
	}
}
