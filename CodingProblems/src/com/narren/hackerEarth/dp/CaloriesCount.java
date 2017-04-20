package com.narren.hackerEarth.dp;

import java.util.Scanner;

/**
 * 
 * Working out *
Summer is coming! It's time for Iahub and Iahubina to work out, as they both want to look hot at the beach.
The gym where they go is a matrix a with n lines and m columns. Let number a[i][j] represents the calories burned by performing workout at
the cell of gym in the i-th line and the j-th column.

Iahub starts with workout located at line 1 and column 1. He needs to finish with workout a[n][m]. After finishing workout a[i][j],
he can go to workout a[i + 1][j] or a[i][j + 1]. Similarly, Iahubina starts with workout a[n][1] and she needs to finish with workout a[1][m].
After finishing workout from cell a[i][j], she goes to either a[i][j + 1] or a[i - 1][j].

There is one additional condition for their training. They have to meet in exactly one cell of gym. At that cell, none of them will work out.
They will talk about fast exponentiation (pretty odd small talk) and then both of them will move to the next workout.

If a workout was done by either Iahub or Iahubina, it counts as total gain. Please plan a workout for Iahub and Iahubina such as total gain
to be as big as possible. Note, that Iahub and Iahubina can perform workouts with different speed, so the number of cells that they use to
reach meet cell may differs.

Input
The first line of the input contains two integers n and m (3 ≤ n, m ≤ 1000). Each of the next n lines contains m integers: j-th number
from i-th line denotes element a[i][j] (0 ≤ a[i][j] ≤ 105).

Output
The output contains a single number — the maximum total gain possible.

Examples
input
3 3
100 100 100
100 1 100
100 100 100
output
800
Note
Iahub will choose exercises a[1][1] → a[1][2] → a[2][2] → a[3][2] → a[3][3]. Iahubina will choose exercises a[3][1] → a[2][1] → a[2][2] → a[2][3] → a[1][3].


https://www.hackerearth.com/practice/algorithms/dynamic-programming/2-dimensional/tutorial/

http://codeforces.com/problemset/problem/429/B

 * @author naren
 *
 */
public class CaloriesCount {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int M = sc.nextInt();
        long[][] workout = new long[N + 1][M + 1];
        for(int i = 1; i <= N; i++) {
            for(int j = 1; j <= M; j++) {
                workout[i][j] = sc.nextInt();
            }
        }
        
        System.out.println(getMaxCalories(workout, N, M));
	}

	static long getMaxCalories(long[][] workout, int N, int M) {{
		// Boy from [1][1] to [i][j]
		long[][] boy1 = new long[2 * N][2 * M];
		// Boy from [i][j] to [N][M]
		long[][] boy2 = new long[2 * N][2 * M];
		// Girl from [N][1] to [i][j]
		long[][] girl1 = new long[2 * N][2 * M];
		// Girl from [i][j] to [1][M]
		long[][] girl2 = new long[2 * N][2 * M];

		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= M; j++) {
				boy1[i][j] = Math.max(boy1[i - 1][j], boy1[i][j - 1]) + workout[i][j];
			}
		}

		for(int i = N; i >= 1; i--) {
			for(int j = M; j >= 1; j--) {
				boy2[i][j] = Math.max(boy2[i + 1][j], boy2[i][j + 1]) + workout[i][j];
			}
		}

		for(int i = N; i >= 1; i--) {
			for(int j = 1; j <= M; j++) {
				girl1[i][j] = Math.max(girl1[i + 1][j], girl1[i][j - 1]) + workout[i][j];
			}
		}

		for(int i = 1; i <= N; i++) {
			for(int j = M; j >= 1; j--) {
				girl2[i][j] = Math.max(girl2[i - 1][j], girl2[i][j + 1]) + workout[i][j];
			}
		}

		/**
		 * Comparing the 4 sequences of the boy and the girl, the boy and girl meet only at one position (i,j), iff

         Boy: (i,j-1)-->(i,j)-->(i,j+1) and Girl: (i+1,j)-->(i,j)-->(i-1,j)
         or
         Boy: (i-1,j)-->(i,j)-->(i+1,j) and Girl:  (i,j-1)-->(i,j)-->(i,j+1)
		 *
		 *
		 */

		long maxCalories = 0;
		for(int i = 2; i < N ; i++) {
			for(int j = 2; j < M; j++) {
				long c1 = boy1[i][j - 1] + boy2[i][j + 1] + girl1[i + 1][j] + girl2[i - 1][j];

				long c2 = boy1[i - 1][j] + boy2[i + 1][j] + girl1[i][j - 1] + girl2[i][j + 1];

				maxCalories = Math.max(maxCalories, Math.max(c1, c2));
			}
		}
		return maxCalories;
	}
	}

}
