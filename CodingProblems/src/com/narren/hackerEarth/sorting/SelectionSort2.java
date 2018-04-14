package com.narren.hackerEarth.sorting;

public class SelectionSort2 {

	
	public static void main(String[] args) {
		SelectionSort2 ss2 = new SelectionSort2();
		int[] arr = new int[]{5,3,2,7,9, 4, 1};
		ss2.selectionSort(arr);
		System.out.println(arr);
	}
	
	void swap(int[] arr, int i, int j) {
		int t = arr[i];
		arr[i] = arr[j];
		arr[j] = t;
	}
	
	void selectionSort(int[] arr) {
		for(int i = 0; i < arr.length; i++) {
			int swapIndex = i;
			for(int j = i + 1; j < arr.length; j++) {
				if(arr[j] < arr[swapIndex]) {
					swapIndex = j;
				}
			}
			swap(arr, i, swapIndex);
		}
	}
}
