package com.narren.hackerRank;

import java.util.Scanner;

public class Cookies {

	static int[]arr;
	static int count;
	static boolean[] visited;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		arr = new int[N];
		visited = new boolean[N];
		for(int i = 0; i < N; i++) {
			arr[i] = sc.nextInt();
		}
		process(0);
	}

	static int process(int i) {
		if (i >= arr.length) {
			return 0;
		}
		if(i < (arr.length - 1)) {
			if (arr[i] < arr[i + 1]) {
				count += 1;	
			} else if (arr[i] > arr[i + 1]) {
				count += process(i + 1);	
			} else {
				count += process(i + 1);
			} 
		} else {
			return 1;
		}
		return 1;
	}
}
