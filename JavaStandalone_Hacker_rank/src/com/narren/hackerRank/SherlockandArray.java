package com.narren.hackerRank;

import java.util.Scanner;

public class SherlockandArray {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int T = in.nextInt();
		while (T > 0) {
			int N = in.nextInt();
			int[] array = new int[N];
			int pivotIndex = N/2;
			int sumRight = 0, sumLeft = 0;
			for (int i = 0; i < N; i++) {
				int element = in.nextInt();
				if (i < pivotIndex) {
					sumLeft += element;
				} else if (i > pivotIndex) {
					sumRight += element;
				}
				array[i] = element;				
			}
			process(array, pivotIndex, sumLeft, sumRight);
			T--;
		}
	}
	
	static void process (int[] array, int pivotIndex, int sumLeft, int sumRight) {
		if (sumLeft < sumRight) {
			for (int i = pivotIndex + 1 ; i < array.length ; i++) {
				sumLeft += array[i - 1];
				sumRight -= array[i];
				if (sumLeft == sumRight) {
					System.out.println("YES");
					return;
				} if (sumLeft > sumRight) {
					System.out.println("NO");
					return;
				}
			}
			System.out.println("NO");
		} else if (sumLeft > sumRight) {
			for (int i = pivotIndex - 1 ; i > 0 ; i--) {
				sumLeft -= array[i];
				sumRight += array[i + 1];
				if (sumLeft == sumRight) {
					System.out.println("YES");
					return;
				} if (sumLeft < sumRight) {
					System.out.println("NO");
					return;
				}
			}
			System.out.println("NO");
		} else {
			System.out.println("YES");
		}
	}
}
