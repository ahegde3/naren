package com.narren.hackerEarth.sorting;

public class QuickSort {

	void printArray(int[] arr) {
        for(int i : arr) {
        	System.out.print(i + " ");
        }
        System.out.println();
    }
    int partitionIndex(int[] arr, int s, int e) {
        int partitionIndex = s;
        int pivot = arr[e];

        for(int i = s; i < e; i++) {
            if(arr[partitionIndex] < pivot) {
                swap(arr, i, partitionIndex);
                partitionIndex++;
            }
        }
        swap(arr, partitionIndex, e);
        printArray(arr);

        return partitionIndex;
    }

    void quickSort(int[] arr, int start, int end) {
        if(start < end) {
            int i = partitionIndex(arr, start, end);
            quickSort(arr, start, i - 1);
            quickSort(arr, i + 1, end);
        }
    }
    void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    public static void main(String[] args) {
		QuickSort qc = new QuickSort();
		int[] arr = new int[]{5, 4, 3, 2, 7, 1};
		qc.quickSort(arr, 0, arr.length - 1);
	}
}
