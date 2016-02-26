package com.narren.hackerRank;

import java.math.BigInteger;
import java.util.Scanner;

public class FibonacciModified {

	static int A;
	static int B;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		A = sc.nextInt();
		B = sc.nextInt();
		int n = sc.nextInt();
		BigInteger[] arr = new BigInteger[n + 1];
		System.out.println(process(n - 1, arr));
	}
	
	private static BigInteger process(int n, BigInteger[] arr) {
		if(n == 0) {
			return new BigInteger("" + A);
		}
		if(n == 1) {
			return new BigInteger("" + B);
		}
		
		if (arr[n] != null) {
			return arr[n];
		}
		arr[n] = process(n - 1, arr).pow(2).add(process(n - 2, arr));
		return arr[n];
	}
}
