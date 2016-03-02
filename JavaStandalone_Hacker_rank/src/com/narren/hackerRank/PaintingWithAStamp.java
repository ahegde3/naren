package com.narren.hackerRank;

import java.util.Scanner;

/**
 * 
1
5 5
_ _ _ # #
_ _ _ # #
_ _ _ _ _
# # _ _ _
# # _ _ _

 * @author naren
 *
 */
public class PaintingWithAStamp {

	static int[][] arr;
	static int R;
	static int C;
	static int maxSquare;
	
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while(T > 0) {
			R = sc.nextInt();
			C = sc.nextInt();
			arr = new int[R][C];
			for(int i = 0; i < R; i++) {
				for(int j = 0; j < C; j++) {
					String c = sc.next();
					// '_' = 1, '?' = 2, '#' = 3
					arr[i][j] = c.charAt(0) == '_' ? 1 : c.charAt(0) == '?' ? 2 : 3 ;
				}
			}
			T--;
		}
	}
	
	static void process() {
		for(int i = 0; i < C; i++) {
			int count = 0;
			boolean start = false;
			boolean end = false;
			int si = -1;
			int ei = -1;
			for(int j = 0; j < R; j++) {
				if(arr[i][j] == 1 || arr[i][j] == 2) {
					if(!start) {
						si = j;
					}
					start = true;
					count++;
				} else {
					if (start) {
						ei = j - 1;
						break;
					}
				}
			}
			if (count > 1) {
				
			} else if (count == 1) {
				// Check whether this is a '_' or '?'
				if (arr[si][i] == 2) {
					// we can ignore a '?'
					continue;
				} else {
					//just return from here, max square is 1
					maxSquare = 1;
					return;
					
				}
				
			} else {
				continue;
			}
			
		}
	}
}
