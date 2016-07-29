package sotong;

import java.util.Scanner;

/**
 * For problem description and examples refer to screen shots in this folder
 * whose name starts with "ConstructionOfBaseStation_"
 * 
2
5 3
300 410 150 55 370
120 185 440 190 450
165 70 95 420 50
5 5
356 55 41 453 12
401 506 274 506 379
360 281 421 311 164
138 528 461 477 470
 * @author naren
 *
 */
public class ConstructionOfBaseStation {

	static Cell[] stack;
	static int maxSum = Integer.MIN_VALUE;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while (T > 0) {
			int W = sc.nextInt();
			int H = sc.nextInt();
			int[][] arr = new int[W][H];
			stack = new Cell[(W + 1) * (H + 1)];
			for(int j = 0; j < H; j++) {
				for(int i = 0; i < W; i++) {
					arr[i][j] = sc.nextInt();
				}
			}
			process(arr, W, H);
			System.out.println(maxSum * maxSum);
			maxSum = Integer.MIN_VALUE;
			T--;
		}
	}

	public static void process(int[][] arr, int W, int H) {
		for(int i = 0; i < W; i++) {
			for(int j = 0; j < H; j++) {
				findMaxSum(arr, W, H, i, j);
			}
		}
	}

	public static void findMaxSum(int[][] arr, int W, int H, int cw, int ch) {
		int totalSum = 0;
		Stack stack = new Stack();
		Cell root = new Cell(cw, ch);
		Cell[] rootNeighbors = getNeighbors(arr, W, H, cw, ch);
		boolean[][] visited = new boolean[W][H];
		int i = 0;
		stack.push(root);
		while(i < rootNeighbors.length && rootNeighbors[i] != null) {
			// Iterate over all the neighbors
			Cell biggestNeighbor = getBiggestNeighbor(arr, rootNeighbors, visited);
			stack.push(biggestNeighbor);
			visited[biggestNeighbor.r][biggestNeighbor.c] = true;
			totalSum += arr[biggestNeighbor.r][biggestNeighbor.c];
			int j = 2;
			while(j < 4) {
				Cell cell = stack.pop();
				Cell[] neighbors = getNeighbors(arr, W, H, cell.r, cell.c);
				Cell biggest = getBiggestNeighbor(arr, neighbors);
				totalSum += arr[biggest.r][biggest.c];
				stack.push(biggest);
				j++;
			}
			totalSum += arr[cw][ch];
			maxSum = Math.max(totalSum, maxSum);
		}
		stack.tail = 0;
		stack.head = 0;
		stack = null;

	}

	public static Cell getBiggestNeighbor(int[][] arr, Cell[] neighbors, boolean[][] visited) {
		Cell biggestNeighbor = null;
		int maxNum = Integer.MIN_VALUE;
		for(int i = 0; i < neighbors.length; i++) {
			if(neighbors[i] != null && arr[neighbors[i].r][neighbors[i].c] > maxNum
					&& !visited[neighbors[i].r][neighbors[i].c]) {
				maxNum = arr[neighbors[i].r][neighbors[i].c];
				biggestNeighbor = neighbors[i];
			}
		}
		return biggestNeighbor;
	}

	public static Cell getBiggestNeighbor(int[][] arr, Cell[] neighbors) {
		Cell biggestNeighbor = null;
		int maxNum = Integer.MIN_VALUE;
		for(int i = 0; i < neighbors.length; i++) {
			if(neighbors[i] != null && arr[neighbors[i].r][neighbors[i].c] > maxNum) {
				maxNum = arr[neighbors[i].r][neighbors[i].c];
				biggestNeighbor = neighbors[i];
			}
		}
		return biggestNeighbor;
	}

	public static Cell[] getNeighbors(int[][] arr, int w, int h, int wRow, int hCol) {
		Cell[] neighbors = new Cell[6];
		int i = 0;
		// We have two patterns for finding the neighbors, based on whether the row is even or odd
		if((wRow & 1) == 0) {
			//Even
			if(wRow - 1 >= 0) {
				neighbors[i++] = new Cell(wRow - 1, hCol);
			}
			if(wRow + 1 < w) {
				neighbors[i++] = new Cell(wRow + 1, hCol);
			}
			if(hCol - 1 >= 0) {
				neighbors[i++] = new Cell(wRow, hCol - 1);
			}
			if(hCol + 1 < h) {
				neighbors[i++] = new Cell(wRow, hCol + 1);
			}
			if(wRow - 1 >= 0 && hCol - 1 >= 0) {
				neighbors[i++] = new Cell(wRow - 1, hCol - 1);
			}
			if(wRow + 1 < w && hCol - 1 >= 0) {
				neighbors[i++] = new Cell(wRow + 1, hCol - 1);
			}
		} else {
			// Odd row
			if(hCol - 1 >= 0) {
				neighbors[i++] = new Cell(wRow, hCol - 1);
			}
			if(hCol + 1 < h) {
				neighbors[i++] = new Cell(wRow, hCol + 1);
			}
			if(wRow - 1 >= 0) {
				neighbors[i++] = new Cell(wRow - 1, hCol);
			}
			if(wRow + 1 < w) {
				neighbors[i++] = new Cell(wRow + 1, hCol);
			}
			if(wRow - 1 >= 0 && hCol + 1 < h) {
				neighbors[i++] = new Cell(wRow - 1, hCol + 1);
			}
			if(wRow + 1 < w && hCol + 1 < h) {
				neighbors[i++] = new Cell(wRow + 1, hCol + 1);
			}
		}
		return neighbors;

	}


	static class Cell {
		int r;
		int c;
		public Cell(int x, int y) {
			r = x;
			c = y;
		}
	}
	static class Stack {
		static int head = 0;
		static int tail = 0;

		static boolean isEmpty() {
			return head == 0 && tail == 0;
		}

		static void push(Cell cell) {
			if(isEmpty()) {
				tail++;
			}
			head++;
			stack[head] = cell;
		}

		static Cell pop() {
			Cell cell = null;
			if(isEmpty()) {
				return null;
			}
			cell = stack[head];
			head--;
			if(head == tail) {
				head = 0;
				tail = 0;
			}
			return cell;
		}
	}
}
