package com.narren.hackerRank;

import java.util.Scanner;

/**
 * 
3
5 5
_ _ _ # #
_ _ _ # #
_ _ _ _ _
# # _ _ _
# # _ _ _
5 6
# # _ _ # _
_ _ _ _ # _
_ _ _ _ # _
_ _ _ _ _ _
# _ # # _ _
5 6
# # # ? # ?
_ _ _ _ # ?
_ _ _ _ # ?
# _ _ _ _ _
# _ ? _ _ _



Expected : 
3
1
2 

 * @author naren
 *
 */
public class PaintingWithAStamp {

	static int[][] arr;
	static int R;
	static int C;
	static int maxSquare = Integer.MAX_VALUE;
	static boolean[][] explored;
	static boolean unexplored;
	static boolean onlyOne;
	static boolean onlyTwo;
	static boolean mixBag;
	static boolean optionalHit;


	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while(T > 0) {
			R = sc.nextInt();
			C = sc.nextInt();
			arr = new int[R][C];
			explored = new boolean[R][C];
			for(int i = 0; i < R; i++) {
				for(int j = 0; j < C; j++) {
					String c = sc.next();
					// '_' = 1, '?' = 2, '#' = 3
					arr[i][j] = c.charAt(0) == '_' ? 1 : c.charAt(0) == '?' ? 2 : 3 ;
				}
			}
			process();
			System.out.println(maxSquare);
			 maxSquare = Integer.MAX_VALUE;
			T--;
		}
	}

	static void process() {
		for(int i = 0; i < R; i++) {
			for(int j = 0; j < C; j++) {
				int count = explore(i, j);
				if(unexplored && (!onlyTwo)) {
					maxSquare = Math.min(count, maxSquare);
					if(maxSquare == 1) {
						return;
					}
				}
				
			}
		}
	}

	static int explore(int i, int j) {
		int tempCount = 0;
		int row = i;
		int col = j;
		boolean foundBreak = false;
		unexplored = false;
		boolean outBound = false;
		onlyOne = false;
		onlyTwo = false;
		mixBag = false;

		while(true) { 
			for (int r = i; r <= row; r++) {
				for(int c = j; c <= col; c++) {
					if (r >= R || c >= C) {
						outBound = true;
						break;
					}
					if(arr[r][c] == 3) {
						foundBreak = true;
						break;
					}
					
					if(arr[r][c] == 2) {
						onlyTwo = true;
					}
					if(arr[r][c] == 1) {
						onlyOne = true;
					}
					
					if(!explored[r][c]) {
						unexplored = true;
						explored[r][c] = true;
					}
					
				}
				if (outBound) {
					break;
				}
				if(foundBreak) {
					break;
				}
			}
			if (outBound) {
				break;
			}
			
			
			if(foundBreak) {
				break;
			} else {
				row++;
				col++;
				tempCount++;
			}
		}
		if (onlyOne && onlyTwo) {
			mixBag = true;
		}
		return tempCount;
	}
}
