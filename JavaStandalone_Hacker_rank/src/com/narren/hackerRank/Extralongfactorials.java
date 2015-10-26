package com.narren.hackerRank;

import java.math.BigInteger;
import java.util.Scanner;

public class Extralongfactorials {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		System.out.println(factorial(new BigInteger("" + N, 10)));
	}
	static BigInteger factorial (BigInteger in) {
		if (in.equals(new BigInteger("1", 10))) {
			return in;
		}
		return in.multiply(factorial(in.subtract(new BigInteger("1", 10))));
	}

}
