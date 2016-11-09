package com.narren.leetCode;
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
		int i = algo.findKthLargest(new int[]{1}, 1);
		System.out.println(i);
	}
	public int findKthLargest(int[] nums, int k) {
		quickSort(nums, 0, nums.length - 1);
		for(int i = nums.length - 1, j = 1; i >= 0 ; i++) {
			if(j == k) {
				return nums[i];
			}
			j++;
		}
		return 0;
	}

	void quickSort(int[] arr, int start, int end) {
		while(start < end) {
			int partitionIndex = partitionIndex(arr, start, end);
			quickSort(arr, start, partitionIndex - 1);
			quickSort(arr, partitionIndex + 1, end);
		}
	}
	
	int partitionIndex(int[] arr, int start, int end) {
		int pivot = arr[end];
		int partitionIndex = start;
		for(int i = start; i < end; i++) {
			if(arr[i] <= pivot) {
				swap(arr, i, partitionIndex);
				partitionIndex++;
			}
		}
		swap(arr, end, partitionIndex);
		return partitionIndex;
	}
	
	void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}
