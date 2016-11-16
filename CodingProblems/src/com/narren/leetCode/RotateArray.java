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
		int[] res = rotateArray(new int[]{1,2,3,4,5}, 3);
	}
	static int[] rotateArray(int[] input, int order) {
		if(order > input.length) {
			order = order % input.length;
		}
		int len = input.length - order;
		reverse(0, len - 1, input);
		reverse(len, input.length - 1, input);
		reverse(0, input.length - 1, input);
		
		return input;
	}
	
	static void reverse(int start, int end, int[] arr) {
		if(start >= end) {
			return;
		}
		while(start < end) {
			int temp = arr[start];
			arr[start] = arr[end];
			arr[end] = temp;
			start++;
			end--;
		}
	}
}
