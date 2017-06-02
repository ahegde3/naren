package com.narren.hackerEarth.sorting;

import java.util.Scanner;

/**
You have been given an 
A
A array consisting of 
N
N integers. All the elements in this array are guaranteed to be unique. For each position 
i
i in the array 
A
A you need to find the position 
A
[
i
]
A[i] should be present in, if the array was a sorted array. You need to find this for each 
i
i and print the resulting solution.

Input Format:

The first line contains a single integer 
N
N denoting the size of array 
A
A. The next line contains 
N
N space separated integers denoting the elements of array 
A
A.

Output Format:

Print 
N
N space separated integers on a single line , where the 
I
Ith integer denotes the position of 
A
[
i
]
A[i] if this array were sorted.

Constraints:

1
≤
N
≤
100
1≤N≤100
1
≤
A
[
i
]
≤
100
 * @author naren
 *
 */
public class InsertionSort {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int[] arr = new int[N];
		for(int i = 0; i < N; i++) {
			arr[i] = sc.nextInt();
		}
		insertionSort(arr);
		for(int i : arr) {
			System.out.print(i + " ");
		}
	}
	
	static void insertionSort(int[] arr) {
		for(int i = 0; i < arr.length; i++) {
			int temp = arr[i];
			int j = i;
			while(j > 0 && temp < arr[j - 1]) {
				swap(arr, j, j - 1);
				j--;
			}
			
		}
	}
	
	static void swap(int[] arr, int i, int j) {
		int t = arr[i];
		arr[i] = arr[j];
		arr[j] = t;
	}
}