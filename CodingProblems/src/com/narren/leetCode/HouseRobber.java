package com.narren.leetCode;
/**
 * 
You are a professional robber planning to rob houses along a street.
Each house has a certain amount of money stashed, the only constraint stopping you from
robbing each of them is that adjacent houses have security system connected
and it will automatically contact the police if two adjacent houses were broken into on the same night.

Given a list of non-negative integers representing the amount of money of each house,
determine the maximum amount of money you can rob tonight without alerting the police.
 * 
https://leetcode.com/problems/house-robber/

 * @author naren
 *
 */
public class HouseRobber {
    public int rob(int[] nums) {
    	if(nums.length < 1) {
            return 0;
        }
        if(nums.length == 1) {
            return nums[0];
        }
        if(nums.length == 2) {
            return Math.max(nums[0], nums[1]);
        }
        int[] sum = new int[nums.length + 1];
        sum[1] = nums[0];
        sum[2] = Math.max(nums[0], nums[1]);
        for(int i = 3; i < sum.length; i++) {
            sum[i] = Math.max(sum[i - 2], sum[i - 3]) + nums[i - 1];
        }
        return Math.max(sum[sum.length - 1], sum[sum.length - 2]);
    }

}
