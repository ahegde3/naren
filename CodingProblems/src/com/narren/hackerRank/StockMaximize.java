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
		long profit = 0;
		long buyingPrice = 0;
		long buyingUnit = 0;
		long maxPrice = arr[arr.length - 1];
        for(int i = arr.length - 2; i >= 0; i--) {
            if(maxPrice != Math.max(maxPrice, arr[i])) {
                profit += maxPrice * buyingUnit - buyingPrice;
                maxPrice = arr[i];
                buyingPrice = 0;
                buyingUnit = 0;
            } else {
                buyingUnit++;
                buyingPrice += arr[i];
            }
        }
        profit += maxPrice * buyingUnit - buyingPrice;
        return profit;
		
	}
}
