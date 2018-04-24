package com.narren.leetCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sum3 {

	
	public static void main(String[] args) {
		Sum3 sum = new Sum3();
		//-2,0,1,1,2
		System.out.println(sum.threeSum(new int[]{0,0,0}));
	}
	public List<List<Integer>> threeSum(int[] nums) {
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		Arrays.sort(nums);
		int k = nums.length - 1;
		for(int i = 0; i <= nums.length - 3; i++) {
			
			for(int j = i + 1; j <= nums.length - 2 && j < k;) {
				int sum = nums[i] + nums[j] + nums[k];
				if(sum == 0) {
					ArrayList<Integer> l = new ArrayList<>();
					l.add(nums[i]);
					l.add(nums[j]);
					l.add(nums[k]);
					list.add(l);
					i++;
					j++;
					k--;
				} else if(sum > 0) {
					k--;
				} else {
					j++;
				}
				
			}
			
		}		
		return list;
	}
}
