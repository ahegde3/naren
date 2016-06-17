package sotong;

import java.util.Scanner;

public class Locust {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int[][] arr = new int[N][100000000];
		int maxRight = 0;
		for(int i = 0; i < N; i++) {
			int intervals = sc.nextInt();
			for(int j = 1; j <=intervals; j++) {
				int start = sc.nextInt();
				int end = sc.nextInt();
				maxRight = Math.max(end, maxRight);
			}
		}
		process(arr, maxRight);
	}
	
	static void process(int[][] arr, int maxRight) {
		
	}
}
