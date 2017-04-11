package com.narren.hackerEarth;

import java.util.Scanner;

public class Factorial {
    static int MOD = 1000000007;
    public static void main(String args[] ) throws Exception {
        Scanner s = new Scanner(System.in);
        int N = s.nextInt();
        long[] arr = new long[100002];
    	arr[0] = 1;
    	arr[1] = 1;
    	arr[2] = 2;
        while(N > 0) {
        int num = s.nextInt();
        long res = getFactorial(num, arr);
        System.out.println(res);
        N--;
        }
        
    }
    
    static long getFactorial(int n, long[] arr) {
    	long temp = 1;
    	for(int i = n; i > 0; i--) {
    		if(arr[i - 1] != 0) {
    			temp = (((temp * i) % MOD) * arr[i - 1]) % MOD;
    			break;
    		} else {
    			temp = (temp * i) % MOD;
    		}
    		
    	}
    	arr[n] = temp;
    	return arr[n];
    }
}