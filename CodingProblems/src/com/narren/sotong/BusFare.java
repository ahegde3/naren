package com.narren.sotong;

import java.util.Scanner;

public class BusFare {

	/**
	 *
In a city, a bus stops every kilometer. Also the bus fare is different from 1km to 10km,
so you can use each section well to decrease the total fare to get to the destination. 
For example, let�s suppose a bus fare list as below:

12 21 31 40 49 58 69 79 90 101

According to the list, the minimum fare would be 147 for 15km: 1 ticket for 3km + 2 tickets for 6km.
Or 3 tickets for 5km will have the same result. Other ways cost more. When the bus fare and distance are given,
calculate the minimum fare that you can move the given distance. 
	 *
	 *
Sample input
2
12 21 31 40 49 58 69 79 90 101
15
12 20 30 40 25 60 70 80 90 11
21

Expected output
147
34
	 */
	static int[] fare = new int[11];
	static int[] rate;
	static int N;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while(T > 0) {
			for(int i = 1; i < 11; i++ ) {
				fare[i] = sc.nextInt();
			}
			N = sc.nextInt();
			rate = new int[N + 1];
			for(int i = 1; i <= N; i++) {
				rate[i] = Integer.MAX_VALUE;
			}

			process();
			System.out.println(rate[N]);
			T--;
		}
	}
	
	static void process() {
		for(int i = 1; i < 11; i++) {
			for (int j = 1; j <= N; j++) {
				if(i > j) {
					continue;
				}
				
				rate[j] = Math.min(rate[j], rate[j - i] + fare[i]);
			}
		}
	}
}
