package com.narren.leetCode;

import java.util.PriorityQueue;

/**
 * 
https://leetcode.com/problems/kth-largest-element-in-an-array/
 * 
Find the kth largest element in an unsorted array. Note that it is the kth largest element in the sorted order, not the kth distinct element.

For example,
Given [3,2,1,5,6,4] and k = 2, return 5.

Note: 
You may assume k is always valid, 1 ≤ k ≤ array's length.
 * 
 * @author ns.bisht
 *
 */
public class KthLargestElement {

	public static void main(String[] args) {
		KthLargestElement algo = new KthLargestElement();
		int i = algo.findKthLargest(new int[]{2,1}, 1);
		System.out.println(i);
	}
	
	public int findKthLargest(int[] nums, int k) {
		PriorityQueue<Integer> queue = new PriorityQueue<Integer>(nums.length);
		for(int n : nums) {
			queue.add(n);
		}
		int counter = 0;
		while(counter <= nums.length - k) {
			int n = queue.poll();
			if(counter == nums.length - k) {
				return n;
			}
			counter++;
		}
		return 0;
	}
}
