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
	static int count = Integer.MAX_VALUE;
	static Cost[][] costArr;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		int C = 1;
		while(C <= T) {
			int N = sc.nextInt();
			//int[][] arr = new int[N][100000000];
			costArr = new Cost[N][50];
			int maxRight = 0;
			Cost[][] mainArr = new Cost[N][1002];
			for(int i = N - 1; i >= 0; i--) {
				int intervals = sc.nextInt();
				mainArr[i][0] = new Cost(0, 0, intervals);
				for(int j = 1; j <=intervals; j++) {
					int start = sc.nextInt();
					int end = sc.nextInt();
					if(i == 0) {
						mainArr[i][j] = new Cost(start, end, 1);
					} else {
						mainArr[i][j] = new Cost(start, end, Integer.MAX_VALUE);	
					}
					maxRight = Math.max(end, maxRight);
				}
			}
			long startTime = System.currentTimeMillis();
			int res = processJumps(mainArr, maxRight);
			long endTime = System.currentTimeMillis();
			System.out.println("time taken=" + (endTime - startTime));
			//int res = process(arr, maxRight);
			System.out.println("Case #" + C);
			System.out.println(res);
			C++;
		}
	}

	static int process(int[][] arr, int maxRight) {
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
		if(jump > count) {
			return Integer.MAX_VALUE;
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

	static int processJumps(Cost[][] arr, int maxRight) {
		for(int i = 1; i < arr.length - 1; i ++) {
			for(int j = 1; j <= arr[i][0].jumps; j++) {
				boolean overlap = false;
				int start = arr[i][j].start;
				int end = arr[i][j].end;
				for(int k = i - 1; k >= 0; k--) {
					for(int l = 1; l <= arr[k][0].jumps; l++) {
						if(start <= arr[k][l].end &&
								end >= arr[k][l].start) {
							arr[i][j].jumps = Math.min(arr[i][j].jumps, arr[k][l].jumps + 1);
							//System.out.println(arr[i][j].start + "-" + arr[i][j].end + "->" + arr[i][j].jumps);
							if(start >= arr[k][l].start && end <= arr[k][l].end) {
								overlap = true;
								break;
							} else {
								if(start < arr[k][l].start && end < arr[k][l].end) {
									// left
									end = end - arr[k][l].start;
								} else if (start > arr[k][l].start && end > arr[k][l].end ) {
									// right 
									start = arr[k][l].end - start;
								}
							}
						} else if(end < arr[k][l].start) {
							// break
							break;
						} 
					}
					if(overlap) {
						break;
					}
				}

			}
		}

		// Now process the bottom of the platform
		int minStep = Integer.MAX_VALUE;
		int row = arr.length - 1;
		for(int i = 0; i <= maxRight; i++) {
			int tempStep = 0;
			for(int j = 1; j < arr[row][0].jumps; j++) {
				if(arr[row][j].start <= i && arr[row][j].end >= i) {
					tempStep++;
				}
			}
			boolean oneFound = false;
			for(int k = row - 1; k >= 0; k--) {
				for(int l = 1; l <= arr[k][0].jumps; l++) {
					if(arr[k][l].start <= i && arr[k][l].end >= i) {
						minStep = Math.min(minStep, tempStep + arr[k][l].jumps);
						oneFound = true;
						break;
					}
				}
				if(oneFound) {
					break;
				}
			}
		}
		//System.out.println("###### " + minStep);
		return minStep;
	}

	static class Cost {
		int start;
		int end;
		int jumps;

		public Cost(int s, int e, int j) {
			this.start = s;
			this.end = e;
			this.jumps = j;
		}
	}
}
