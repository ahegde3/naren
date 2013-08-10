package com.narren.hackerRank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MarksAndToys {

	private static int solve(ArrayList<Integer> itemPrice, int K) {
		int totalItem = 0;
		int totalPrice = 0;
		Collections.sort(itemPrice);
		for (Integer price : itemPrice) {
			totalPrice += price;
			if (totalPrice <= K) {
				totalItem++;
			} else {
				break;
			}
		}
		return totalItem;
	}

	public static void main(String[] args) {
		int N;
		int K = 0;
		ArrayList<Integer> itemPrice;

		Scanner in = new Scanner(System.in);
		N = in.nextInt();
		K = in.nextInt();
		itemPrice = new ArrayList<Integer>(N);

		for (int i = 0; i < N; i++) {
			itemPrice.add(i, in.nextInt());
			}
		System.out.println(solve(itemPrice, K));
	}
}
