package com.narren.hackerRank;

import java.util.Scanner;

public class QuickSort2Sorting {

	static void quickSort(int[] ar, int start, int end) {
		if (start < end) {
			int partitonIndex = partition(ar, start, end);
			quickSort(ar, start, partitonIndex - 1);
			quickSort(ar, partitonIndex + 1, end);
		}
	}   

	static int partition(int[] ar, int start, int end) {
		int pivot = ar[end];
		int partitionIndex = start;
		for (int i = start; i < end; i++) {
			if (ar[i] <= pivot) {
				swap(ar, i, partitionIndex);
				partitionIndex++;
			}
		}
		swap(ar, end, partitionIndex);
		printArray(ar);
		return partitionIndex;
	}
	static void swap(int[]ar, int first, int second) {
		int temp = ar[first];
		ar[first] = ar[second];
		ar[second] = temp;
	}
	static void printArray(int[] ar) {
		for(int n: ar){
			System.out.print(n+" ");
		}
		System.out.println("");
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		int[] ar = new int[n];
		for(int i=0;i<n;i++){
			ar[i]=in.nextInt(); 
		}
		quickSort(ar, 0, ar.length - 1);
	}    
}
