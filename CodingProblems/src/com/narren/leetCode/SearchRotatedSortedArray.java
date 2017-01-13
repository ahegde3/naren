package com.narren.leetCode;

/**
 * 
Suppose an array sorted in ascending order is rotated at some pivot unknown to you beforehand.

(i.e., 0 1 2 4 5 6 7 might become 4 5 6 7 0 1 2).

You are given a target value to search. If found in the array return its index, otherwise return -1.

You may assume no duplicate exists in the array.

https://leetcode.com/problems/search-in-rotated-sorted-array/
 * 
 * @author naren
 *
 */

public class SearchRotatedSortedArray {
	public static void main(String[] args) {
		SearchRotatedSortedArray ins = new SearchRotatedSortedArray();
		System.out.println(ins.search(new int[]{3, 4, 5, 6, 7, 1, 2}, 4));

	}

	public int search(int[] nums, int target) {
        int start = 0;
        int end = nums.length - 1;

        while(start <= end) {
            int mid = (start + end)/2;

            if(nums[mid] == target) {
                return mid;
            }
            if(mid - 1 > start && nums[mid - 1] == target) {
                return mid - 1;
            }
            if(mid + 1 < end && nums[mid + 1] == target) {
                return mid + 1;
            }
            if(nums[start] == target) {
                return start;
            }
            if(nums[end] == target) {
                return end;
            }

            if(nums[mid] > nums[start] && nums[mid] > nums[end]) {
                // left side sort
                if(target < nums[mid] && target > nums[start]) {
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }

            } else if(nums[mid] < nums[start] && nums[mid] < nums[end]) {
                //right side sort
                if(target > nums[mid] && target < nums[end]) {
                    start = mid + 1;
                } else {
                    end = mid - 1;
                }

            } else {
                //no rotation
                if(nums[mid] > target) {
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }
            }
        }
        return -1;
	}
}
