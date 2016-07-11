package sotong;

import java.util.Scanner;

/**
 * 
There is a mobile piece and a stationary piece on the N×M chessboard.
The available moves of the mobile piece are the same as set out in the image below.
<<Refer to chess.png file in this directory>>
You need to capture the stationary piece by moving the mobile piece with the minimum amount of moves.

Write a program to find out the minimum number moves to catch a piece. 

Time limit:1 second (java: 2 seconds)

[Input]
Several test cases can be included in the inputs. T, the number of cases is given in the first row of the inputs.
After that, the test cases as many as T (T = 20) are given in a row. 
N, the numbers of the rows and M, the number of columns of the chessboard are given in the first row of each test case. 
R & C is the location information of the attacking piece and S & K is the location of the defending pieces and are
given in the row at the second line.
However, the location of the uppermost end of the left end is (1, 1)

[Output]
Output the minimum number of movements to catch a defending piece at the first line of each test case. If not moveable, output equals -1. 

[I/O Example]

Input 
2
9 9
3 5 2 8
20 20
2 3 7 9

Output
2

5

Sample input
5
10 10
1 1 10 10
20 20
2 3 7 9
30 30
2 15 29 29
40 40
2 3 1 40
45 45
40 10 27 40

 * @author nsbisht
 *
 */
public class Chess {

	static int startR;
	static int startC;
	static int endR;
	static int endC;
	static int N;
	static int M;
	static boolean started;
	static boolean[][] visited;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		int testCase = 1;
		while(testCase <= T) {
			N = sc.nextInt();
			M = sc.nextInt();
			int[][] arr = new int[N + 1][M + 1];
			visited = new boolean[N + 1][M + 1];
			startR = sc.nextInt();
			startC = sc.nextInt();
			endR = sc.nextInt();
			endC = sc.nextInt();
			int res = minimumSteps(startR, startC, 0, false);
			if(res == Integer.MAX_VALUE) {
				res = -1;
			}
			System.out.println(res);
			testCase++;
		}
	}

	static int minimumSteps(int curR, int curC, int steps, boolean isGoingFar) {
		if(visited[curR][curC]) {
			return Integer.MAX_VALUE;
		}
		if(started) {
			if(curR == startR && curC == startC) {
				return Integer.MAX_VALUE;
			}
		}
		started = true;
		if(curR < 1 || curC < 1 || curR > N || curC > M) {
			return Integer.MAX_VALUE;
		}
		if(curR == endR && curC == endC) {
			return steps;
		}
		visited[curR][curC] = true;
		if(Math.abs(curR - endR) >=2 || Math.abs(curC - endC) >= 2) {
			if(isGoingFar) {
				return Integer.MAX_VALUE;
			}
			// Do in a certain direction
			if(curR == endR && curC < endC) {
				// Go right
				int a = minimumSteps(curR - 1 , curC + 2, steps + 1, isGoingFar);
				int b = minimumSteps(curR + 1 , curC + 2, steps + 1, isGoingFar);
				int c = minimumSteps(curR + 2 , curC + 1, steps + 1, isGoingFar);
				int d = minimumSteps(curR - 2 , curC + 1, steps + 1, isGoingFar);

				int e = Math.min(a, b);
				int f = Math.min(c, d);
				return Math.min(e, f);

			} else if(curR > endR && curC < endC) {
				// Go right top
				int a = minimumSteps(curR - 2 , curC + 1, steps + 1, isGoingFar);
				int b = minimumSteps(curR - 1 , curC + 2, steps + 1, isGoingFar);
				return Math.min(a, b);
			} else if(curR < endR && curC < endC) {
				// Go right down
				int a = minimumSteps(curR + 1 , curC + 2, steps + 1, isGoingFar);
				int b = minimumSteps(curR + 2 , curC + 1, steps + 1, isGoingFar);
				return Math.min(a, b);
			} else if(curC == endC && curR > endR) {
				// Go right and left up
				int a = minimumSteps(curR - 1 , curC + 2, steps + 1, isGoingFar);
				int b = minimumSteps(curR - 2 , curC + 1, steps + 1, isGoingFar);
				int c = minimumSteps(curR - 2 , curC - 1, steps + 1, isGoingFar);
				int d = minimumSteps(curR - 1 , curC - 2, steps + 1, isGoingFar);
				int e = Math.min(a, b);
				int f = Math.min(c, d);
				return Math.min(e, f);
			} else if(curC > endC && curR > endR) {
				// Go left up
				int a = minimumSteps(curR - 2 , curC - 1, steps + 1, isGoingFar);
				int b = minimumSteps(curR - 1 , curC - 2, steps + 1, isGoingFar);
				return Math.min(a, b);
			} else if(curR == endR && curC > endC) {
				// Go left
				int a = minimumSteps(curR - 2 , curC - 1, steps + 1, isGoingFar);
				int b = minimumSteps(curR - 1 , curC - 2, steps + 1, isGoingFar);
				int c = minimumSteps(curR + 1 , curC - 2, steps + 1, isGoingFar);
				int d = minimumSteps(curR + 2 , curC - 1, steps + 1, isGoingFar);
				int e = Math.min(a, b);
				int f = Math.min(c, d);
				return Math.min(e, f);
			} else if(curR < endR && curC > endC) {
				// Go left down
				int a = minimumSteps(curR + 1 , curC - 2, steps + 1, isGoingFar);
				int b = minimumSteps(curR + 2 , curC - 1, steps + 1, isGoingFar);
				return Math.min(a, b);
			} else if(curC == endC && curR < endR) {
				// Go down
				int a = minimumSteps(curR + 1 , curC - 2, steps + 1, isGoingFar);
				int b = minimumSteps(curR + 2 , curC - 1, steps + 1, isGoingFar);
				int c = minimumSteps(curR + 1 , curC + 2, steps + 1, isGoingFar);
				int d = minimumSteps(curR + 2 , curC + 1, steps + 1, isGoingFar);
				int e = Math.min(a, b);
				int f = Math.min(c, d);
				return Math.min(e, f);
			}

		} else {
			// Go any direction
			// Do in a certain direction
			if(curR == endR && curC < endC) {
				// Go right
				int a = minimumSteps(curR - 1 , curC + 2, steps + 1, true);
				int b = minimumSteps(curR + 1 , curC + 2, steps + 1, true);
				int c = minimumSteps(curR + 2 , curC + 1, steps + 1, true);
				int d = minimumSteps(curR - 2 , curC + 1, steps + 1, true);

				int e = Math.min(a, b);
				int f = Math.min(c, d);
				return Math.min(e, f);

			} else if(curR > endR && curC < endC) {
				// Go right top
				int a = minimumSteps(curR - 2 , curC + 1, steps + 1, true);
				int b = minimumSteps(curR - 1 , curC + 2, steps + 1, true);
				return Math.min(a, b);
			} else if(curR < endR && curC < endC) {
				// Go right down
				int a = minimumSteps(curR + 1 , curC + 2, steps + 1, true);
				int b = minimumSteps(curR + 2 , curC + 1, steps + 1, true);
				int c = minimumSteps(curR + 1 , curC - 2, steps + 1, true);
				int d = minimumSteps(curR + 2 , curC - 1, steps + 1, true);
				return Math.min(Math.min(a, b), Math.min(c, d));
			} else if(curC == endC && curR > endR) {
				// Go right and left up
				int a = minimumSteps(curR - 1 , curC + 2, steps + 1, true);
				int b = minimumSteps(curR - 2 , curC + 1, steps + 1, true);
				int c = minimumSteps(curR - 2 , curC - 1, steps + 1, true);
				int d = minimumSteps(curR - 1 , curC - 2, steps + 1, true);
				int e = Math.min(a, b);
				int f = Math.min(c, d);
				return Math.min(e, f);
			} else if(curC > endC && curR > endR) {
				// Go left up
				int a = minimumSteps(curR - 2 , curC - 1, steps + 1, true);
				int b = minimumSteps(curR - 1 , curC - 2, steps + 1, true);
				return Math.min(a, b);
			} else if(curR == endR && curC > endC) {
				// Go left
				int a = minimumSteps(curR - 2 , curC - 1, steps + 1, true);
				int b = minimumSteps(curR - 1 , curC - 2, steps + 1, true);
				int c = minimumSteps(curR + 1 , curC - 2, steps + 1, true);
				int d = minimumSteps(curR + 2 , curC - 1, steps + 1, true);
				int e = Math.min(a, b);
				int f = Math.min(c, d);
				return Math.min(e, f);
			} else if(curR < endR && curC > endC) {
				// Go left down
				int a = minimumSteps(curR + 1 , curC - 2, steps + 1, true);
				int b = minimumSteps(curR + 2 , curC - 1, steps + 1, true);
				return Math.min(a, b);
			} else if(curC == endC && curR < endR) {
				// Go down
				int a = minimumSteps(curR + 1 , curC - 2, steps + 1, true);
				int b = minimumSteps(curR + 2 , curC - 1, steps + 1, true);
				int c = minimumSteps(curR + 1 , curC + 2, steps + 1, true);
				int d = minimumSteps(curR + 2 , curC + 1, steps + 1, true);
				int e = Math.min(a, b);
				int f = Math.min(c, d);
				return Math.min(e, f);
			}

		}
		return Integer.MAX_VALUE;
	}
}
