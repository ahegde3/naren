package com.narren.hackerRank;

import java.util.Scanner;

public class Interstellar {

	/**
	 * 
	 * 
	 * 
5
0
0 0 60 60
1
0 0 2 0
1 0 1 2 0
1
0 0 60 60
40 40 20 20 10
2
100 50 10 5
80 40 10 6 10
80 10 70 40 5
3
500 500 1000 1000
501 501 999 999 1000
1 1 499 499 100
1000 999 0 0 200

answers:
120
2
90
41
305

	 */
	static int[][] arr;
	static int D;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while(T > 0) {
			int N = sc.nextInt();
			D = 2 + N*2 + 1;
			arr = new int[D][D];
			int x1 = sc.nextInt();
			int y1 = sc.nextInt();
			int x2 = sc.nextInt();
			int y2 = sc.nextInt();
			int dis = Math.abs(x2 - x1) + Math.abs(y2 - y1);
			arr[1][2] = dis;
			arr[2][1] = dis;

			for (int i = 3; i < D;) {
				int xi1 = sc.nextInt();
				int yi1 = sc.nextInt();
				int xi2 = sc.nextInt();
				int yi2 = sc.nextInt();
				int d = sc.nextInt();

				arr[i][i + 1] = d;
				arr[i+1][i] = d;

				arr[1][i] = Math.abs(xi1 - x1) + Math.abs(yi1 - y1);
				arr[i][1] = Math.abs(xi1 - x1) + Math.abs(yi1 - y1);

				arr[1][i + 1] = Math.abs(xi2 - x1) + Math.abs(yi2 - y1);
				arr[i + 1][1] = Math.abs(xi2 - x1) + Math.abs(yi2 - y1);

				arr[2][i] = Math.abs(xi1 - x2) + Math.abs(yi1 - y2);
				arr[i][2] = Math.abs(xi1 - x2) + Math.abs(yi1 - y2);

				arr[2][i + 1] = Math.abs(xi2 - x2) + Math.abs(yi2 - y2);
				arr[i + 1][2] = Math.abs(xi2 - x2) + Math.abs(yi2 - y2);

				i = i + 2;
			}

			process();
			System.out.println(arr[1][2]);
			T--;
		}
	}
	
	static void process() {
		for(int k = 1; k < D; k++) {
			for(int i = 1; i < D; i++) {
				for(int j = 1; j < D; j++) {
					if (arr[i][k] + arr[k][j] < arr[i][j]) {
						arr[i][j] = arr[i][k] + arr[k][j];
					}
				}
			}
			
		}
	}
}
