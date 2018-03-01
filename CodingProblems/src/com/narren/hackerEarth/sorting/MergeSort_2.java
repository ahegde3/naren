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
		
	}
}
