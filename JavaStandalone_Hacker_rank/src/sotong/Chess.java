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
	static Cell[][] parents;
	static int[] xVal = new int[]{-2, -1, 1, 2, 2, 1, -1, -2};
	static int[] yVal = new int[]{1, 2, 2, 1, -1, -2, -2, -1};
	static Cell[] queue;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		int testCase = 1;
		while(testCase <= T) {
			N = sc.nextInt();
			M = sc.nextInt();
			queue = new Cell[(N + 2) * (M + 2)];
			int[][] arr = new int[N + 1][M + 1];
			visited = new boolean[N + 1][M + 1];
			parents = new Cell[N + 1][M + 1];
			startR = sc.nextInt();
			startC = sc.nextInt();
			endR = sc.nextInt();
			endC = sc.nextInt();
			int res = bfsChess(arr);
			started = false;
			if(res == 0/*Integer.MAX_VALUE*/) {
				res = -1;
			}
			System.out.println(res);
			testCase++;
		}
	}
	static int bfsChess(int[][] arr) {
		Queue q = new Queue();
		q.push(new Cell(startR, startC));
		visited[startR][startC] = true;
		while(!q.isEmpty() && parents[endR][endC] == null) {
			Cell cell = q.pop();
			if(cell == null) {
				continue;
			}
			for(int i = 0; i < 8; i++) {
				int x = cell.xVal + xVal[i];
				int y = cell.yVal + yVal[i];
				if(needsVisit(x, y)) {
					parents[x][y] = cell;
					q.push(new Cell(x, y));
					visited[x][y] = true;
				}
			}
		}
		Cell cell = parents[endR][endC];
		int counter = 0;
		while(cell != null){
			cell = parents[cell.xVal][cell.yVal];
			counter++;
		}
		parents = null;
		visited = null;
		queue = null;
		q.head = 0;
		q.tail = 0;
		q = null;
		return counter;
	}

	static boolean needsVisit(int x, int y) {
		if(x > 0 && y > 0 && x <= N && y <= M) {
			return !visited[x][y];
		}
		return false;
	}
	static class Cell {
		int xVal;
		int yVal;
		public Cell(int x, int y) {
			xVal = x;
			yVal = y;
		}
	}
	static class Queue {
		private static int head = -1;
		private static int tail = -1;

		public boolean isEmpty() {
			return head == -1 && tail == -1;
		}
		public void push(Cell cell) {
			if(isEmpty()) {
				tail++;
			}
			head++;
			queue[head] = cell;
		}
		public Cell pop() {
			Cell cell = null;
			if(!isEmpty()) {
				cell = queue[tail];
				if(tail == head) {
					tail = -1;
					head = -1;
				} else {
					tail++;					
				}
			}
			return cell;
		}
		public static Cell peek() {
			return queue[tail];
		}
	} 
}
