package com.narren.hackerEarth.greedy;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 
 * You are given container full of water. Container can have limited amount of water. You also have 
N bottles to fill. You need to find the maximum numbers of bottles you can fill.

SAMPLE INPUT 
1
5 10
8 5 4 3 2
SAMPLE OUTPUT 
3
 * @author naren
 *
 */
public class BeingGreedyforWater {
	
	static int maxBotttle = 0; 

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		
		while(T > 0) {
			int N = sc.nextInt();
			int X = sc.nextInt();
			int[] bottles = new int[N];

			for(int i = 0; i < N; i++) {
				bottles[i] = sc.nextInt();
			}
			Arrays.sort(bottles);
			for(int i = 0; i < N; i++) {
				fillWater(bottles, i, 0, X, 0);				
			}
			System.out.println(maxBotttle);
			maxBotttle = 0;
			
			T--;
		}
	}
	
	static int fillWater(int[] b, int index, int sum, int X, int bottles) {
		if(index >= b.length) {
			return sum;
		}
		if(b[index] + sum == X) {
			maxBotttle = Math.max(maxBotttle, bottles + 1);
			return 0;
		}
		
		if(b[index] + sum > X) {
			return -2;
		}
		int c = fillWater(b, index + 1, sum + b[index], X, bottles + 1);
		
		if(c == -2) {
			return index + 1;
		} else if(c == 0) {
			return index + 1;
		} else {
			return fillWater(b, c, sum + b[index], X, bottles + 1);
		}
	}
}
