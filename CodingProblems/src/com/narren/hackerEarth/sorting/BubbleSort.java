package com.narren.hackerEarth.sorting;

import java.util.Scanner;

/**
 * You have been given an array 
A
A of size 
N
N . you need to sort this array non-decreasing oder using bubble sort. However, you do not need to print the sorted array . You just need to print the number of swaps required to sort this array using bubble sort

Input Format

The first line consists of a single integer 
N
N denoting size of the array. The next line contains 
N
N space separated integers denoting the elements of the array.

Output Format Print the required answer in a single line

Constrains 
1
≤
N
≤
100
1≤N≤100
1
≤
a
[
i
]
≤
100
 * @author naren
 *
 */
public class BubbleSort {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int[] arr = new int[N];
		for(int i = 0; i < N; i++) {
			arr[i] = sc.nextInt();
		}
		System.out.println(bubbleSort(arr));
	}

	static int bubbleSort(int[] arr) {
		int swaps = 0;
		boolean sorted = false;
		while(!sorted) {
			sorted = true;
			for(int i = 0; i < arr.length; i++) {
				if(i + 1 < arr.length) {
					if(arr[i] > arr[i + 1]) {
						sorted = false;
						swaps++;
						swap(arr, i, i + 1);
					}
				}

			}
		}
		return swaps;
	}

	static void swap(int[] arr, int i, int j) {
		int t = arr[i];
		arr[i] = arr[j];
		arr[j] = t;
	}
}