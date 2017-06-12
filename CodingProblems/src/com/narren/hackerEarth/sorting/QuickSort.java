package com.narren.hackerEarth.sorting;

import java.util.Scanner;

public class QuickSort {

	void printArray(long[] arr) {
        for(long i : arr) {
        	System.out.print(i + " ");
        }
    }
    int partitionIndex(long[] arr, int s, int e) {
        int partitionIndex = s;
        long pivot = arr[e];

        for(int i = s; i < e; i++) {
            if(arr[i] < pivot) {
                swap(arr, i, partitionIndex);
                partitionIndex++;
            }
        }
        swap(arr, partitionIndex, e);
        printArray(arr);

        return partitionIndex;
    }

    void quickSort(long[] arr, int start, int end) {
        if(start < end) {
            int i = partitionIndex(arr, start, end);
            quickSort(arr, start, i - 1);
            quickSort(arr, i + 1, end);
        }
    }
    void swap(long[] arr, int i, int j) {
        long temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    public static void main(String[] args) {
    	Scanner sc = new Scanner(System.in);
    	int N = sc.nextInt();
    	long[] arr = new long[N];
    	for(int i = 0; i < N; i++) {
    		arr[i] = sc.nextLong();
    	}
		QuickSort qc = new QuickSort();
		qc.quickSort(arr, 0, arr.length - 1);
		qc.printArray(arr);
	}
}
