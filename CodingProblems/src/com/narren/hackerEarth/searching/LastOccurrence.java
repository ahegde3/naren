package com.narren.hackerEarth.searching;

import java.util.Scanner;

public class LastOccurrence {
	public static void main(String args[] ) throws Exception {
		//Scanner
        Scanner s = new Scanner(System.in);
        int N = s.nextInt();
        int M = s.nextInt();

        int last = -1;
        for (int i = 1; i <= N; i++) {
            int x = s.nextInt();
            if(x == M) {
            	last = i;
            }
        }

		System.out.println(last);
	}
}
