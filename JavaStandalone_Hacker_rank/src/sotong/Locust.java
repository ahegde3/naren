package sotong;

import java.util.Scanner;

public class Locust {

	/**
	 * 
1
5
3 0 2 7 10 13 17
2 1 5 9 11
2 7 12 14 18
1 3 7
2 4 6 15 16
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		int C = 1;
		while(C <= T) {
			int N = sc.nextInt();
			int maxH = N;
			int[][] arr = new int[N][100000000];
			int maxRight = 0;
			for(int i = 0; i < N; i++) {
				int intervals = sc.nextInt();
				for(int j = 1; j <=intervals; j++) {
					int start = sc.nextInt();
					int end = sc.nextInt();
					for(int k = start ; k <= end ; k++) {
						arr[maxH - 1][k] = 1;
					}
					maxRight = Math.max(end, maxRight);
				}
				maxH--;
			}
			int res = process(arr, maxRight);
			System.out.println("Case #" + C);
			System.out.println(res);
			C++;
		}
	}
	
	static int process(int[][] arr, int maxRight) {
		int count = Integer.MAX_VALUE;
		//for(int row = arr.length - 1; row >= 0; row-- )
		for(int col = 0; col < maxRight; col++) {
			if(arr[0][col] != 0) {
				count = Math.min(count, getJumpCount(arr, col, 0, 0));	
			}
		}
		return count;
	}
	
	static int nextStep(int[][] arr, int row, int col) {
		if(row < 0) {
			return -1;
		}
		
		while(row < arr.length) {
			if(arr[row][col] == 1) {
				return row;
			}
			row++;
		}
		return arr.length;
	}
	
	static int getJumpCount(int arr[][], int curCol, int curRow, int jump) {
		if(curRow == -1) {
			return jump;
		}
		int next = nextStep(arr, curRow + 1, curCol);
		if(next == -1) {
			return Integer.MAX_VALUE;
		}
		if(next == arr.length) {
			return jump + 1;
		}
		int jumpCount = getJumpCount(arr, curCol, next, jump + 1);
		return jumpCount;
	}
}
