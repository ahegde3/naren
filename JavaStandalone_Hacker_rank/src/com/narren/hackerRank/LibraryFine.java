package com.narren.hackerRank;

import java.util.Scanner;

/**
 * 28 2 2015
 15 4 2015
 * @author naren
 *
 */
public class LibraryFine {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int DA = sc.nextInt();
		int MA = sc.nextInt();
		int YA = sc.nextInt();

		int DE = sc.nextInt();
		int ME = sc.nextInt();
		int YE = sc.nextInt();

		if (YA > YE) {
			System.out.println(10000);
			return;
		} else if (YA < YE) {
			System.out.println(0);
			return;
		}
		if (MA > ME) {
			System.out.println(500 * (MA - ME));
			return;
		} else if (MA < ME) {
			System.out.println(0);
			return;
		} 
		if (DA > DE) {
			System.out.println(15 * (DA - DE));
			return;
		} 
		{
			System.out.println(0);
		}
	}

}
