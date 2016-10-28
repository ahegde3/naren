package com.narren.sotong;

import java.util.Scanner;

/**
 * For problem description and examples refer to screen shots in this folder
 * whose name starts with "ConstructionOfBaseStation_"
 * 
5
5 3
300 410 150 55 370
120 185 440 190 450
165 70 95 420 50
5 5
356 55 41 453 12
401 506 274 506 379
360 281 421 311 489
425 74 276 371 164
138 528 461 477 470
13 3
197 51 443 274 47 552 160 96 501 102 469 318 308
516 128 506 471 381 418 328 517 380 78 569 58 90
113 238 179 444 541 27 444 62 264 93 245 353 37
7 11
292 182 586 607 259 190 239
511 716 425 367 511 462 714
593 713 231 60 118 442 82
626 577 579 682 136 176 681
240 23 410 193 230 729 109
453 231 287 383 444 578 409
729 401 408 330 213 574 54
684 224 75 62 660 472 227
606 37 473 487 222 185 476
84 477 158 94 141 484 122
616 333 302 626 29 99 674
15 15
488 923 92 659 783 908 167 332 467 205 457 871 536 189 642
676 729 520 687 276 13 709 305 315 621 19 606 201 722 671
631 829 973 318 487 140 411 633 530 981 594 372 787 586 895
520 938 375 770 495 310 59 820 840 785 457 454 967 178 507
498 368 377 326 247 79 875 38 778 800 205 186 131 543 948
672 530 848 342 397 751 192 265 763 447 869 223 950 636 34
669 929 802 500 979 978 322 185 598 618 663 192 746 289 44
77 271 943 874 211 532 441 567 396 141 527 286 755 95 206
458 803 319 490 384 736 328 977 954 651 975 472 405 344 189
624 725 838 159 624 269 400 855 63 924 349 711 473 115 446
937 359 820 851 629 698 437 834 18 257 632 534 153 478 908
205 875 185 508 373 826 432 487 522 10 663 870 711 566 941
773 663 954 237 166 957 722 198 346 337 708 592 443 809 41
634 174 193 733 800 227 418 503 903 405 261 805 234 461 191
176 891 203 825 575 508 627 845 610 814 263 159 719 450 903

 * @author naren
 *
 */
public class ConstructionOfBaseStation {

	static int maxSum = Integer.MIN_VALUE;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while (T > 0) {
			int W = sc.nextInt();
			int H = sc.nextInt();
			int[][] arr = new int[W][H];
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
		Cell root = new Cell(cw, ch);
		Cell[] rootNeighbors = getNeighbors(arr, W, H, cw, ch);
		boolean[][] visited = new boolean[W][H];
		int i = 0;
		visited[root.r][root.c] = true;
		while(i < rootNeighbors.length && rootNeighbors[i] != null) {
			boolean[][] secondVisited = new boolean[W][H];
			secondVisited[root.r][root.c] = true;

			// Iterate over all the neighbors
			Cell biggestNeighbor1 = getBiggestNeighbor(arr, rootNeighbors, visited);
			if(biggestNeighbor1 != null) {
				visited[biggestNeighbor1.r][biggestNeighbor1.c] = true;
				secondVisited[biggestNeighbor1.r][biggestNeighbor1.c] = true;
				totalSum += arr[biggestNeighbor1.r][biggestNeighbor1.c];
			}


			Cell[] neighbors2 = getNeighbors(arr, W, H, biggestNeighbor1.r, biggestNeighbor1.c);
			Cell biggestNeighbor2 = getBiggestNeighbor(arr, neighbors2, secondVisited);
			if(biggestNeighbor2 != null) {
				secondVisited[biggestNeighbor2.r][biggestNeighbor2.c] = true;
			}

			Cell secondBiggestRoot = getBiggestNeighbor(arr, rootNeighbors, secondVisited);

			if(secondBiggestRoot != null && biggestNeighbor2 != null) {
				if(arr[secondBiggestRoot.r][secondBiggestRoot.c] > arr[biggestNeighbor2.r][biggestNeighbor2.c]) {
					totalSum += arr[secondBiggestRoot.r][secondBiggestRoot.c];

					secondVisited[biggestNeighbor2.r][biggestNeighbor2.c] = false;	
					secondVisited[secondBiggestRoot.r][secondBiggestRoot.c] = true;

					Cell[] neighbors3 = getNeighbors(arr, W, H, secondBiggestRoot.r, secondBiggestRoot.c);
					Cell biggestNeighbor3 = getBiggestNeighbor(arr, neighbors3, secondVisited);
					if(biggestNeighbor3 != null)
						secondVisited[biggestNeighbor3.r][biggestNeighbor3.c] = true;

					Cell rootBiggest = getBiggestNeighbor(arr, rootNeighbors, secondVisited);
					if(rootBiggest != null)
						secondVisited[rootBiggest.r][rootBiggest.c] = true;

					Cell secondRootBiggest = getBiggestNeighbor(arr, neighbors2, secondVisited);

					if(secondRootBiggest != null && rootBiggest != null && biggestNeighbor3 != null)
						totalSum += Math.max(Math.max(arr[biggestNeighbor3.r][biggestNeighbor3.c], arr[rootBiggest.r][rootBiggest.c]),
								arr[secondRootBiggest.r][secondRootBiggest.c]);

				} else {
					totalSum += arr[biggestNeighbor2.r][biggestNeighbor2.c];


					Cell[] neighbors3 = getNeighbors(arr, W, H, biggestNeighbor2.r, biggestNeighbor2.c);
					Cell biggestNeighbor3 = getBiggestNeighbor(arr, neighbors3, secondVisited);
					if(biggestNeighbor3 != null) 
						secondVisited[biggestNeighbor3.r][biggestNeighbor3.c] = true;

					Cell rootBiggest = getBiggestNeighbor(arr, rootNeighbors, secondVisited);
					if(rootBiggest != null) {
						secondVisited[rootBiggest.r][rootBiggest.c] = true;	
					}


					Cell secondRootBiggest = getBiggestNeighbor(arr, neighbors2, secondVisited);

					if(secondRootBiggest != null && biggestNeighbor3 != null) {
						totalSum += Math.max(Math.max(arr[biggestNeighbor3.r][biggestNeighbor3.c], arr[secondRootBiggest.r][secondRootBiggest.c]),
								rootBiggest == null ? -1 : arr[rootBiggest.r][rootBiggest.c]);
					}
				}
			}

			totalSum += arr[cw][ch];
			maxSum = Math.max(totalSum, maxSum);
			totalSum = 0;
			i++;
		}
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
}
