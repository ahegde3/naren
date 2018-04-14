package com.narren.hackerEarth.sorting;

public class InsertionSort2 {

	public static void main(String[] args) {
		InsertionSort2 is2 = new InsertionSort2();
		int[] arr = new int[]{31, 2, 7, 8, 3, 1};
		is2.insertionSort(arr);
		System.out.println(arr);
	}

	void insertionSort(int[] arr) {
		for(int i = 1; i < arr.length; i++) {
			int temp = arr[i];
			for(int j = i - 1; j >= 0; j--) {
				if(arr[j] > temp) {
					arr[j+1] = arr[j];
					arr[j] = temp;
				}
			}
		}
	}
}
