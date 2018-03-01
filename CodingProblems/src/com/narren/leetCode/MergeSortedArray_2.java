package com.narren.leetCode;

public class MergeSortedArray_2 {
	public void merge(int[] nums1, int m, int[] nums2, int n) {
		
		int index = m + n - 1;
		
		if(m < 1) {
			while(index >= 0) {
				nums1[index--] = nums2[--n];
			}
			return;
		} else if(n < 1) {
			return;
		}

		
		while(index >= 0 && m > 0 && n > 0) {
			if(nums1[m - 1] > nums2[n - 1]) {
				nums1[index] = nums1[m - 1];
				m--;
			} else {
				nums1[index] = nums2[n - 1];
				n--;
			}
			index--;
		}
		
		while(n > 0) {
			nums1[n - 1] = nums2[n - 1];
			n--;
		}

	}
	
	public static void main(String[] args) {
		MergeSortedArray_2 ms2 = new MergeSortedArray_2();
		int[] arr = new int[]{2,0};
		ms2.merge(arr, 1, new int[]{1}, 1);
		
		for(int i : arr) {
			System.out.print(i + " ");
		}
	}
}
