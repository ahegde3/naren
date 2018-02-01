package com.narren.leetCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sum3 {

	
	public static void main(String[] args) {
		Sum3 sum = new Sum3();
		System.out.println(sum.threeSum(new int[]{-4,-2,-2,-2,0,1,2,2,2,3,3,4,4,6,6}));
	}
	public List<List<Integer>> threeSum(int[] nums) {
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		
		HashMap<K, V>

		Arrays.sort(nums);

		int length = nums.length;
		for(int i = 0; i < length - 2; i++) {
			if(i > 0 && nums[i] == nums[i - 1]) {
				continue;
			}
			for(int j = i + 1; j < length - 1; j++) {
				if(j > i + 1 && nums[j] == nums[j - 1]) {
					continue;
				}
				int f = 0 - (nums[i] + nums[j]);
				for(int k = j + 1; k < length; k++) {
					if(nums[k] == f) {
						ArrayList<Integer> subList = new ArrayList<Integer>();
						subList.add(nums[i]);
						subList.add(nums[j]);
						subList.add(nums[k]);
						list.add(subList);
						break;
					}
				}
			}
		}
		return list;
	}
}
