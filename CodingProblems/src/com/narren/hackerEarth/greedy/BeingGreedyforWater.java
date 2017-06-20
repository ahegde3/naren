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

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();

		while(T > 0) {
			int N = sc.nextInt();
			long X = sc.nextInt();
			long[] bottles = new long[N];

			for(int i = 0; i < N; i++) {
				bottles[i] = sc.nextInt();
			}
			Arrays.sort(bottles);

			int count = 0;
			for(int j = 0;j < N;j++){
				if(bottles[j] <= X){
					X -= bottles[j];
					count++;
				}
				else
					break;
			}
			System.out.println(count);
			T--;
		}
	}
}
