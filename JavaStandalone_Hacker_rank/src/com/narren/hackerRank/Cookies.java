package com.narren.hackerRank;

import java.util.Scanner;

public class Cookies {

	static int[]arr;
	static int count;
	static int[] visited;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		arr = new int[N];
		visited = new int[N];
		for(int i = 0; i < N; i++) {
			arr[i] = sc.nextInt();
		}
		process();
		int sum = 0;
		for(int i = 0; i < visited.length; i++) {
			sum += visited[i];
		}
		System.out.println(sum);
	}

	static void process() {
		//first iterate forward
		for(int i = 0; i < arr.length; i++) {
			visited[i] = 1;	
			if (i > 0) {
				if (arr[i] > arr[i - 1]) {
					visited[i] = (1 + visited[i - 1]);
				}
			}
		}

		// Now backwards
		for(int i = arr.length - 2; i >= 0; i--) {
			if (arr[i] > arr[i + 1]) {
				visited[i] = Math.max(visited[i], visited[i + 1] + 1);
			}
		}
	}
}
