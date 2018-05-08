package com.narren.hackerEarth.sorting;

public class HeapSort {

	public static void main(String[] args) {
		HeapSort hs = new HeapSort();
		int[] array = new int[]{4, 5, 8, 3, 2, 9, 1};
		hs.heapSort(array);
		System.out.println("");
	}
	
	void heapSort(int[] array) {
		
		buildMaxHeap(array);
		int lastindex = array.length - 1;
		for(int i = 0; i < array.length; i++) {
			swap(array, i, lastindex);
			lastindex--;
			
			heapify(array, lastindex);
		}
		
	}
	
	void buildMaxHeap(int[] array) {
		if(array.length < 1) {
			return;
		}
		
		for(int i = 0; i < array.length; i++) {
			heapify(array, i);
		}
	}
	
	void heapify(int[] array, int i) {
		
		while(i >= 0) {
			int pi = i / 2;
			if(array[pi] < array[i]) {
				swap(array, i, pi);
				i--;
			} else {
				return;
			}
		}
	}
	
	void swap(int[] array, int i, int j) {
		int t = array[i];
		array[i] = array[j];
		array[j] = t;
	}
}
