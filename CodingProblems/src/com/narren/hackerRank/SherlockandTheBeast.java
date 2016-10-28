package com.narren.hackerRank;

import java.util.Scanner;

public class SherlockandTheBeast {
	
	private static void process (int N) {

	
		if (N < 3) {
			System.out.println("-1");
			return;
		}
		int fives = N/3;
		int threes = N%3;
		if (threes == 0) {
			// All fives
			//System.out.println(fives);
			StringBuilder str = new StringBuilder();
			while (fives > 0) {
				str.append("555");
				fives--;
			}
			System.out.println(str);
			return;
		} else {
			int diff = N - 5;
			int counter = 1;
			while (diff >= 3) {
				if (diff % 3 == 0) {
					int totalFives = N - (counter * 5);
					int rep = totalFives / 3;
					StringBuilder str = new StringBuilder();
					while (rep > 0) {
						str.append("555");
						rep--;
					}
					while (counter > 0) {
						str.append("33333");
						counter--;
					}
					System.out.println(str);
					return;
				}
				diff -= 5;
				counter++;
			}
		} 
		
		
		
		
		
		
		if (threes == 2) {
			fives -= 1;
			threes += 3;
			StringBuilder str = new StringBuilder();
			while(fives > 0) {
				str.append("555");
				fives--;
			}
			str.append("33333");
			System.out.println(str);
			return;
		}
		
		if (threes == 1) {
			int newFives = fives/5;
			int newFive = fives%5;
			if ((threes + newFive + newFives)%5 == 0) {
				fives = N - (threes + newFive + newFives);
				StringBuilder str = new StringBuilder();
				while(fives > 0) {
					str.append("555");
					fives--;
				}
				int newNum = (threes + newFive + newFives)/5;
				while (newNum > 0) {
					str.append("33333");
					newNum--;
				}
				System.out.println(str);
				return;
			}
			
		}
		////////
		int nthrees = N/5;
		int nfives = N%5;
		if (nfives == 0) {
			// All fives
			//System.out.println(fives);
			StringBuilder str = new StringBuilder();
			while(nthrees > 0) {
				str.append("33333");
				nthrees--;
			}
			System.out.println(str);
			return;
		}
		if (nfives == 3) {
			StringBuilder str = new StringBuilder();
			str.append("555");
			while(nthrees > 0) {
				str.append("33333");
				nthrees--;
			}
			
			System.out.println(str);
			return;
		}
		System.out.println("-1");
		
	
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		for (int i = 0; i < T; i++) {
			int N = sc.nextInt();
			process(N);
		}
	}
}
