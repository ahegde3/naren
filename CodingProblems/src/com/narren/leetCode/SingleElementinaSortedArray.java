package com.narren.leetCode;
/**
 * 
 * Given a sorted array consisting of only integers where every element appears twice except for one element which appears once.
Find this single element that appears only once.

Example 1:
Input: [1,1,2,3,3,4,4,8,8]
Output: 2
Example 2:
Input: [3,3,7,7,10,11,11]
Output: 10
Note: Your solution should run in O(log n) time and O(1) space.


https://leetcode.com/problems/single-element-in-a-sorted-array/#/description

 * 
 * @author naren
 *
 */
public class SingleElementinaSortedArray {
	
	public static void main(String[] args) {
		System.out.println(new SingleElementinaSortedArray().singleNonDuplicate(new int[]{1,1,2}));
	}
	public int singleNonDuplicate(int[] nums) {
		if(nums.length == 1) {
			return nums[0];
		}
		return getDup(nums, 0, nums.length - 1);
	}

	int getDup(int[] num, int s, int e) {
		if(s >= e) {
			return num[s];
		}
		int mid = s + (e - s) / 2;
		int left = num[mid - 1];
		int right = num[mid + 1];
		if(left == num[mid]) {
			if(((mid + 1) & 1) == 1) {
				e = mid -1;
			} else {
				s = mid + 1;
			}
		} else if(right == num[mid]) {
			if(((mid + 1) & 1) == 1) {
				s = mid + 1;
			} else {
				e = mid -1;
			}
		} else {
			return num[mid];
		}

		return getDup(num, s, e);
	}
}
