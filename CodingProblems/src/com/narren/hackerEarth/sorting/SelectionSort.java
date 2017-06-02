package com.narren.hackerEarth.sorting;

import java.util.Scanner;

/**
Consider an Array 
a
a of size 
N
N
Iterate from 1 to 
N
N
In 
i
t
h
ith iteration select the 
i
t
h
ith minimum and swap it with 
a
[
i
]
a[i]
You are given an array 
a
a, size of the array 
N
N and an integer 
x
x. Follow the above algorithm and print the state of the array after 
x
x iterations have been performed.

Input Format

The first line contains two integer 
N
N and 
x
x denoting the size of the array and the steps of the above algorithm to be performed respectively. The next line contains 
N
N space separated integers denoting the elements of the array.

Output Format

Print 
N
N space separated integers denoting the state of the array after 
x
x steps

Constraints

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
1≤a[i]≤100
1
≤
x
≤
N
 * 
 * @author naren
 *
 */
public class SelectionSort {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int x = sc.nextInt();
		int[] arr= new int[N];
		for(int i = 0; i < N; i++) {
			arr[i] = sc.nextInt();
		}

		for(int i = 0; i < x; i++) {
			int minIndex = i;
			for(int j = i + 1; j < N; j++) {
				if(arr[minIndex] > arr[j]) {
					minIndex = j;
				}
			}
			swap(arr, i, minIndex);
		}
		for(int i : arr) {
			System.out.print(i + " ");
		}
	}

	static void swap(int[] arr, int i, int j) {
		int t = arr[i];
		arr[i] = arr[j];
		arr[j] = t;
	}
}
