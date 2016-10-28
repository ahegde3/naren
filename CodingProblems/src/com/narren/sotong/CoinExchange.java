package com.narren.sotong;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 
Minimum coin problem

Input 
2      ←  There are two test cases.
3      ←  Case #1
1 4 6
8 
6      ←  Case #2
1 4 5 7 16 20
4758 

Output
Case #1
2
Case #2
240 

 */
public class CoinExchange {

	static long[] coins;
	static long[] min;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		int k = 1;
		while(T >= k) {
			int c = sc.nextInt();
			coins = new long[c];
			for(int i = 0; i < c; i++) {
				coins[i] = sc.nextInt();
			}
			int m = sc.nextInt();
			min = new long[m + 1];
			for(int i = 1; i < m + 1; i++) {
				min[i] = Long.MAX_VALUE;
			}
			process();
			System.out.println("Case #" + k);
			if (min[m] == Long.MAX_VALUE)
				System.out.println("impossible");
			else 
				System.out.println(min[m]);
			
			k++;
		}
	}
	
	static void process() {
		Arrays.sort(coins);
		for(int i = 0; i < coins.length; i++) {
			for(long j = coins[i]; j < min.length; j++) {
				min[(int)j] = Math.min((long)min[(int)j], (long)(1 + min[(int) (j - coins[i])]));
			}
		}
	}
}
