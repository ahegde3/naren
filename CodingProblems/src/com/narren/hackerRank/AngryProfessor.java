package com.narren.hackerRank;

import java.util.Scanner;

public class AngryProfessor {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while (T > 0) {
			int N = sc.nextInt();
			int K = sc.nextInt();
			int studentInTime = 0;
			while (N > 0) {
				int num = sc.nextInt();
				if (num <= 0){
					studentInTime++;
				} 
				N--;
			}
			if (studentInTime >= K) {
				System.out.println("NO");
			} else {
				System.out.println("YES");
			}
			T--;
		}
	}
}
