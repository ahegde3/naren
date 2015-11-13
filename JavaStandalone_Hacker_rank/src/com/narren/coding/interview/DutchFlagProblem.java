package com.narren.coding.interview;

public class DutchFlagProblem {
	public static void main(String[] args) {
		int[] A = new int[]{1,2,3,10,5,6,7};
		solve(A, 4);
		print(A);
	}
	
	private static void print (int[] A) {
		for (int i = 0; i < A.length; i++) {
			System.out.print(A[i] + ",");
		}
	}

	private static void swap (int A[], int i, int j) {
		int temp = A[i];
		A[i] = A[j];
		A[j] = temp;
	}
	
	private static void solve(int[] A, int i) {
		int l = 0;
		int u = A.length - 1;
		while (l < u) {
			if (A[l] > A[i] && A[u] < A[i]) {
				swap(A, l, u);
				l++;
				u--;
				continue;
			}
			
			if (A[l] < A[i]) {
				l++;
			}
			
			if (A[u] > A[i]) {
				u--;
			}
			
			if (i == u) {
				if (A[i] <= A[l]) {
					swap(A, i, l);
					i = l;
					l++;
				}
			}
			
			if (i == l) {
				if (A[i] >= A[u]) {
					swap(A, i, u);
					i = u;
					u--;
				}
			}
		}
	}
}
