package com.narren.hackerEarth.sorting;

public class MergeSort_2 {

	void mergeSort(int[] arr, int s, int e) {
		if(s < e) {
			int mid = ((e - s) / 2) + s;
			mergeSort(arr, s, mid);
			mergeSort(arr, mid + 1, e);	
		}

	}
	
	void merge(int[] arr, int s, int e, int mid) {
		int ps = mid;
		while(e > ps) {
			if(arr[e] < arr[mid]) {
				swap(arr, e, mid);
				e--;
			}
		}
	}
	
	void swap(int[] arr, int i, int k) {
		int t = arr[i];
		arr[i] = arr[k];
		arr[k] = t;
	}
	
}
