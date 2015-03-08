package com.narren.hackerRank;

import java.util.Scanner;

public class SherlockandWatson {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int N = in.nextInt();
		int K = in.nextInt();
		int Q = in.nextInt();
		int[] arr = new int[N];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = in.nextInt();
		}
		int temp1 = -1;
		int temp2 = -1;
		int counter = 0;
		for (int i = 0; counter < arr.length; counter ++) {
			int index = getIndex(K, arr.length, i);
			if (temp1 == -1 && temp2 == -1) {
				temp1 = arr[index];
				arr[index] = arr[i];
			} else {
				if (temp1 != -1) {
					temp2 = arr[index];
					arr[index] = temp1;
					temp1 = -1;
				} else if (temp2 != -1) {
					temp1 = arr[index];
					arr[index] = temp2;
					temp2 = -1;
				}
			}
			i = index;
		}
		while (Q > 0) {
			System.out.println(arr[in.nextInt()]);
			Q--;
		}
	}
	
	static int getIndex (int increment, int arrLen, int currIndex) {
		if ((currIndex + increment) <= (arrLen - 1)) {
			//Normal
			return (currIndex + increment);
		} else {
			//Loop over
			int r = (currIndex + increment);
			while (r > (arrLen - 1)) {
				r = r - arrLen;
			}
			
			return r;
		}
	}
	static void printArray(int[] ar) {
		for(int n: ar){
			System.out.print(n+" ");
		}
		System.out.println("");
	}
}
