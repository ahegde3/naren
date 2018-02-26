package com.narren.hackerEarth.sorting;

public class QuickSort_2 {

	
	public static void main(String[] args) {
		int[] arr = new int[]{4,3,2,1};
		QuickSort_2 qs = new QuickSort_2();
		qs.quickSort(arr, 0, arr.length - 1);
		
		for(int i : arr) {
			System.out.println(i);
		}
	}
	void quickSort(int[] arr, int l, int r) {
		while(l < r) {
			int index = getPartitionIndex(arr, l, r);
			quickSort(arr, l, index - 1);
			quickSort(arr, index + 1, r);
		}
	}
	
	int getPartitionIndex(int[] arr, int l, int r) {
		int p = r;
		
		while(l <= r) {
			if(arr[l] < arr[p]) {
				l++;
				continue;
			}
			if(arr[r] < arr[p]) {
				swap(arr, l, r);
				l++;
				r--;
				continue;
			}
			r--;
		}
		swap(arr, l, p);
		return l;
	}
	
	void swap(int[] arr, int i, int j) {
		int t = arr[i];
		arr[i] = arr[j];
		arr[j] = t;
	}
}
