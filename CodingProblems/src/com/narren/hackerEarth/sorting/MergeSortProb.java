package com.narren.hackerEarth.sorting;

import java.util.Scanner;

/**
 Given an array  A  on size  N N you need to find the number of ordered pairs  (i,j) such that  i < j and  A[i]>A[j].

 https://www.hackerearth.com/practice/algorithms/sorting/merge-sort/practice-problems/algorithm/mergesort/
 * @author naren
 *
 */
public class MergeSortProb {


	static int sum = 0;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int[] arr = new int[N];
		for(int i = 0; i < N; i++) {
			arr[i] = sc.nextInt();
		}
		sort(arr);
		System.out.println(sum);
	}
	public static void sort(int[] arr) {
		mergeSort(arr, 0, arr.length - 1);
	}

	static void mergeSort(int[] arr, int s, int e) {
		if(s < e) {
			int m = (s + e) >>> 1;

			mergeSort(arr, s, m);

			mergeSort(arr, m + 1, e);

			sum += merge(arr, s, m, e);
		}
	}

	static int merge(int arr[ ] , int start, int mid, int end) {
		int sum = 0;
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
				sum += mid + 1 - s1;
			} else {
				temp[index++] = arr[s1];
				s1++;
			}

		}
		index = 0;
		return sum;
//		for(int i = start; i <= end; i++) {
//			arr[i] = temp[index++];
//		}

	}

}
