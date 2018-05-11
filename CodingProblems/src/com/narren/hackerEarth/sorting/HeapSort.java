package com.narren.hackerEarth.sorting;

public class HeapSort {

	public static void main(String[] args) {
		HeapSort hs = new HeapSort();
		int[] array = new int[]{4, 5, 8, 3, 2, 9, 1};
		hs.heapSort(array);
		for(int i : array) {
			System.out.print(i + " ");
		}
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
	
	void heapify(int[] array, int i, int j) {
		
		while(i <= j) {
			int lc = 2 * i + 1;
			int rc = 2 * i + 2;
			int bc = array[lc] > array[rc] ? lc : rc;
			if(bc <= j && array[i] < array[bc]) {
				swap(array, i, bc);
				i++;
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
