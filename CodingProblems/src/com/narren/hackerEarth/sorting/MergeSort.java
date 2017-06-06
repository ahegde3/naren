package com.narren.hackerEarth.sorting;

public class MergeSort {

	public void sort(int[] arr) {

	}

	void mergeSort(int[] arr, int s, int e) {
		if(s > e) {
			int m = (s + e) >>> 1;

			mergeSort(arr, s, m);

			mergeSort(arr, m + 1, e);
			
			merge(arr, s, e, m);
		}
	}

	void merge(int[] arr, int s, int e, int m) {
		for(int i = s; i <= m ; i++) {
			
		}
	}
}
