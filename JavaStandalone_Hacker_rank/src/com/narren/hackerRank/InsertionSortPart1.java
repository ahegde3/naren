package com.narren.hackerRank;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class InsertionSortPart1 {

	static BufferedReader in = new BufferedReader(new InputStreamReader(
			System.in));
	static StringBuilder out = new StringBuilder();
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int S = sc.nextInt();
		int[] arr = new int[S];
		for (int i = 0; i < S; i++) {
			arr[i] = sc.nextInt();
		}
		insertIntoSorted(arr);
		
	}
	public static void insertIntoSorted(int[] ar) {
		int temp = ar[ar.length - 1];
		boolean done = false;
		for (int i = ar.length - 2; i >= 0; i--) {
			if (temp < ar[i]) {
				ar[i + 1] = ar[i];
			} else {
				ar[i + 1] = temp;
				done = true;
			}
			print(ar);
			if (done) {
				break;
			}
		}
		if (!done) {
			ar[0] = temp;
			print(ar);
		}
    }
	private static void print(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}
}
