package com.narren.hackerRank;

import java.util.Scanner;

public class MovingMarbles {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while (T > 0) {
			int totalPockets = sc.nextInt();
			int[] pocket = new int[totalPockets];
			int totalMarbles = 0;
			int tempPockets = totalPockets;
			int counter = 0;
			while (tempPockets > 0) {
				int i = sc.nextInt();
				totalMarbles += i;
				pocket[counter++] = i;
				tempPockets--;
			}
			System.out.println("" + getMoves(pocket, totalMarbles));
			T--;
		}
	}
	
	static int getMoves(int[] pocket, int totalMarbles) {
		if (totalMarbles % pocket.length != 0) {
			return -1;
		}
		int uniform = totalMarbles / pocket.length;
		int attempts = 0;
		for (int i = 0; i < pocket.length; i++) {
			if (pocket[i] > uniform) {
				attempts += (pocket[i] - uniform);
			}
		}
		return attempts;
	}
}
