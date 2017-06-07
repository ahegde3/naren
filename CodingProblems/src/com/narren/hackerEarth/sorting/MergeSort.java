package com.narren.hackerEarth.sorting;

public class MergeSort {

	public static void main(String[] args) {
		MergeSort ms = new MergeSort();
		int[] arr = new int[]{5, 4, 3, 10, 9, 2};
		ms.sort(arr);
		for(int i : arr) {
			System.out.print(i + " ");
		}
	}
	public void sort(int[] arr) {
		mergeSort(arr, 0, arr.length - 1);
	}

	void mergeSort(int[] arr, int s, int e) {
		if(s < e) {
			int m = (s + e) >>> 1;

			mergeSort(arr, s, m);

			mergeSort(arr, m + 1, e);
			
			merge(arr, s, m, e);
		}
	}

	 void merge(int arr[ ] , int start, int mid, int end) {
		 int[] temp = new int[(end - start) + 1];
		 int s1 = start;
		 int s2 = mid + 1;
		 int index = 0;
		 
		 for(int i = start; i <= end; i++) {
			 if(s1 > mid) {
				 temp[index++] = arr[s2];
				 s2++;
				 continue;
			 }
			 if(s2 > end) {
				 temp[index++] = arr[s1];
				 s1++;
				 continue;
				 
			 }
			 if(arr[s1] > arr[s2]) {
				 temp[index++] = arr[s2];
				 s2++;
				 System.out.println("*");
			 } else {
				 temp[index++] = arr[s1];
				 s1++;
			 }
			 
		 }
		 index = 0;
		 for(int i = start; i <= end; i++) {
			 arr[i] = temp[index++];
		 }
		 
	 }
}
