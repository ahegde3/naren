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

	static long maxBotttle = 0; 

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();

		while(T > 0) {
			int N = sc.nextInt();
			int X = sc.nextInt();
			long[] bottles = new long[N];

			for(int i = 0; i < N; i++) {
				bottles[i] = sc.nextInt();
			}
			Arrays.sort(bottles);
			for(int i = 0; i < N; i++) {
				if(bottles[i] > X) {
					break;
				}
				fillWater(bottles, i, 0, X, 1);
			}
			System.out.println(maxBotttle);
			maxBotttle = 0;

			T--;
		}
	}

    static int fillWater(long[] b, int index, long sum, int X, int bottles) {
    	System.out.println(index);
        if(index >= b.length) {
            return -1;
        }
        if(b[index] + sum == X) {
            maxBotttle = Math.max(maxBotttle, bottles);
            return 0;
        }

        if(b[index] + sum > X) {
            return -2;
        }
        for(int i = index + 1; i < b.length; i++) {
        	int c = fillWater(b, i, sum + b[index], X, bottles + 1);
            if(c == -2 || c == -1 || c == 0) {
                return 1;
            }
        }
        return 1;
    }
}
