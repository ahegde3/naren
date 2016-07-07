package sotong;

import java.util.Scanner;

/**
 * 
 A structure consists of a number of floors such that the vertical gaps between floors are constant and each floor is represented by a horizontal line.
 See Figure 1 <<Refer to Locust_Fig_1 file in this directory>>. In each floor, there are disjoint intervals with arbitrary lengths.
The locust can be only on the intervals. 
Specifically, the floors are represented by 0 to N upwards.
Each floor consists of a horizontal line with coordinates starting from 0. Also on the floor i,
there are mi disjoint intervals and each interval is represented by the coordinates of the left and right end points. Especially, on the floor 0,
the whole horizontal line is given as the interval, and on the floor N, there is at least one interval. 
  

 

                                         Figure 1.?  

? 

?If the locust is on an interval, then it can horizontally move to an arbitrary point on the interval.
In particular, on the floor 0, the locust can move arbitrarily, because the whole line is the interval.
Also the locust can jump upwards and when the locust is at a point p on an interval, it can jump and move to the first interval vertically to p.
If it moves vertically and reaches an endpoint of an interval, then it can also move to the interval. For example,
in the left of Figure 2 <<Refer to Locust_Fig_2 file in this directory>>,
the locust can jump to interval 2 at ? of interval 1, but it cannot to interval 3 at that point. But the locust can jump to interval 3 at ? of interval 1.
In the right of Figure 2, the locust can jump from interval 1 to 2 but it cannot move from 1 to 3 by one jump.  


 

?
In the first time, the locust is on the floor 0. The problem is to find the minimum number of jumps so that the
locust can move from the floor 0 to an arbitrary interval of the floor N. 
 

 



 

                                            Figure 2. 

? 

?For example, in Figure 1, the locust can jump from the floor 0 to the first interval of the floor 4.
Then if it jumps to the first interval of the floor 5, it can move to the floor 5 by two jumps. This is the
minimum number of jumps. 


Time Limit: 1 seconds for 20 cases. (java 2 second) (If your program exceeds this time limit, the answers that
have been already printed are ignored and the score becomes 0. So, it may be better to print a wrong answer when
a specific test case might cause your program to exceed the time limit. One guide for the time limit excess would be the size of the input.)
 

[Input]
There can be more than one test case in the input file. The first line has C, the # of test cases.
Then the totally C test cases are provided in the following lines (1 = C = 20). 
Each test case starts with a line containing one integer N(1=N=100) which represents the maximum floor.
The ith (i=1) line of the following N lines contains integers m a1b1...ambm. Here, m is the number of intervals of the floor i and each of
ajbj represents the end points of the intervals from left to right on the floor i. For these integers, on the floor 1 to N-1, 0=m=1,000,
and on the floor N, 1=m=1,000. Also, 0=aj,bj=100,000,000.  
  

[Output] 
For the T-th test case, Case #T should be printed out in the first line, followed by an integer which is the minimum number of jumps
by which the locust can move from the floor 0 to N. 


? 

?
[I/O Example]
Input 
3         ?  There are three test cases.
2         ?  Case #1
1 1 3
1 2 4
2        ?  Case #2 
1 1 5
1 1 5
5        ? Case #3
3 0 2 7 10 13 17
2 1 5 9 11
2 7 12 14 18
1 3 7
2 4 6 15 16

? 

Output
Case #1
1
Case #2
2
Case #3
2
 * 
 * 
Sample input
3
3
1 0 1
0  
1 99999999 100000000 
3 
1 1 2
1 2 3
1 1 4
3
1 1 2
1 2 3
1 1 3
 * @author nsbisht
 *
 */
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
					break;
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
