package com.narren.leetCode;

/**
 * 
Rotate an array of n elements to the right by k steps.

For example, with n = 7 and k = 3, the array [1,2,3,4,5,6,7] is rotated to [5,6,7,1,2,3,4].
 * 
 * @author naren
 *
 */
public class RotateArray {

	public static void main(String[] args) {
		int nums[] = new int[]{1,2,3,4,5};
		/*int[] res = */rotateArray(nums, 3);
		for(int i : nums) {
			System.out.print(i + " ");			
		}
	}
	//	static int[] rotateArray(int[] input, int order) {
	//		if(order > input.length) {
	//			order = order % input.length;
	//		}
	//		int len = input.length - order;
	//		reverse(0, len - 1, input);
	//		reverse(len, input.length - 1, input);
	//		reverse(0, input.length - 1, input);
	//		
	//		return input;
	//	}
	//	
	//	static void reverse(int start, int end, int[] arr) {
	//		if(start >= end) {
	//			return;
	//		}
	//		while(start < end) {
	//			int temp = arr[start];
	//			arr[start] = arr[end];
	//			arr[end] = temp;
	//			start++;
	//			end--;
	//		}
	//	}


	public static void rotateArray(int[] nums, int k) {
		if(k == 0 || (k % nums.length) == 0) {
			return;
		}
		if(k >= nums.length) {
			k %= nums.length;
		}

		for(int i = 0; i < nums.length; i++) {
			if(i - k < 0 || i + k >= nums.length) {
				int replaceIndex = k + i;
				if(replaceIndex >= nums.length) {
					replaceIndex -= nums.length;
				}
				if(i == 0 || k % i != 0) {
					swap(nums, i, replaceIndex);    
				} 
			} 
		}

	}

	static void swap(int[] arr, int i, int j) {
		int t = arr[i];
		arr[i] = arr[j];
		arr[j] = t;
	}
}
