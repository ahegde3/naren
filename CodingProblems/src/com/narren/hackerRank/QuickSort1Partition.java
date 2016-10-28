package com.narren.hackerRank;

import java.util.ArrayList;
import java.util.Scanner;

public class QuickSort1Partition {
	static void partition(int[] ar) {
		int pivot = ar[0];
		ArrayList<Integer> left = new ArrayList<Integer>();
		ArrayList<Integer> right = new ArrayList<Integer>();
		for (int i = 1; i < ar.length; i++) {
			if (ar[i] <= pivot) {
				left.add(ar[i]);
			} else {
				right.add(ar[i]);
			}
		}
		ArrayList<Integer> finalList = new ArrayList<Integer>();
		finalList.addAll(left);
		finalList.add(pivot);
		finalList.addAll(right);
		printArray(finalList.toArray());
	}   

	static void printArray(Object[] ar) {
		for(Object n: ar){
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
		partition(ar);
	}    
}
