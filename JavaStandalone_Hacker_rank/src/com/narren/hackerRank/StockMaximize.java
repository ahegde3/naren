package com.narren.hackerRank;

import java.util.Scanner;

/**
 * 
1
15
5 7 8 10 5 8 9 10 5 6 7 8 9 10 5
 * @author naren
 *
 */
public class StockMaximize {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while(T > 0) {
			int N = sc.nextInt();
			long[] arr = new long[N];
			for(int i = 0; i < arr.length; i++) {
				arr[i] = sc.nextInt();
			}
			System.out.println(process(arr));
			T--;
		}
	}
	
	static long process(long[] arr) {
		long max = 0;
		long start = 0;
		long maxP = 0;
		long cost = 0;
		while(max < arr.length) {
			for(long i = start + 1; i < arr.length; i++) {
				max = Math.max(arr[(int) max], arr[(int) i]) != arr[(int) max] ? i : max;				
			}
			if(start != max && max == arr.length - 1) {
				for(long i = start; i < arr.length - 1; i++) {
					cost += arr[(int) i];				
				}
				maxP += (max - start) * arr[arr.length - 1] - cost;
				return maxP;
			}
			if(start == max) {
				start++;
				max++;
				continue;
			}
			for(long i = start; i < max; i++) {
				cost += arr[(int) i];				
			}
			maxP += (max - start) * arr[(int) max] - cost;
			start = ++max;
			cost = 0;
		}
		return maxP;
		
	}
}
