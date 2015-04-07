package com.narren.hackerRank;

import java.awt.Point;
import java.util.Scanner;

public class MaximalRectangleProblem {

	static void process (int[][] rectangle, int R, int C) {
		Point best_ll = new Point(0, 0);
		Point best_ur = new Point(-1, -1);
		for (int y = 0; y < R; y ++) {
			for (int x = 0; x < C; x++ ) {
				Point ll = new Point (x, y);
				Point ur = grow_ones(ll, rectangle, R, C);
				if (area(ll, ur) > area(best_ll, best_ur)) {
					best_ll = ll;
					best_ur = ur;
				}
			}
		}
		System.out.println(best_ll + " " + best_ur);
		// Variables to keep track of the best rectangle so far: best_11 = (0, 0); best_ur = (-1, -1) 
		//		main algorithm: 
		//		   for 11.x = 0 .. N-1 
		//		      for 11.y = 0 .. M-1 
		//		          ur = grow_ones(11) 
		//		          if area(11, ur)>area(best_11, best_ur) 
		//		             best_11 = 11; best_ur = ur 
		//		end main algorithm 
	}

	static void grow (int x, int y, int[][] rec, int R, int C) {
		if(rec[x][y] == 0) {
			return;
		}
		int x_max = C;
		int x_axis = x;
		int y_axis = y;
		while (y_axis < R) {
			//First lets find the max strech of 1s in x-axis
			while (x_axis < x_max && rec[x_axis+1][y_axis] != 0) {
				x_axis++;
			}
			x_max = (x_axis - x) + 1;
		}
		
		// Now add a layer to it
		y_axis++;
//		while (y_axis) {
//			
//		}
	}
	static Point grow_ones (Point p, int[][] rectangle, int N, int M) {
		Point ur = new Point(p.x - 1, p.y - 1); // Zero area ur-choice 
		int x_max = N; // Right edge of growth zone 
		int y = p.y - 1;
		while (y+1 < M && rectangle[p.x][(y + 1)] != 0) {
			y = y+1;
			int x = p.x;
			while (x+1 < x_max &&  rectangle[x+1][y] != 0) {
				x = x + 1;
			}
			x_max = x;
			if(area(p, new Point(x, y)) > area(p, ur)) {
				ur = new Point(x, y); 
			}
		}
		return ur;
//		define grow_ones(11) 
//		   ur = (11.x-1, 11.y-1) // Zero area ur-choice 
//		   x_max = N // Right edge of growth zone 
//		   y = 11.y-1 
//		   while y+1<M and b[11.x, y+1]!=0 
//		      y = y+1; x = 11.x // Scan a new layer 
//		      while x+1<x<sub> -- </sub>max and b[x+1, y]!=0 
//		         x = x+1 
//		      x_max = x 
//		      if area(11, (x, y))>area(11, ur) 
//		         ur = (x, y) 
//		   return ur 
	}

	static int area(Point ll, Point ur) {
		if (ll.x > ur.x || ll.y > ur.y) {// If ur is left of or 
			return 0;// below 11: return 0
		} else {
			return (ur.x - ll.x +1) * (ur.y - ll.y + 1);
		}
	}
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int R = in.nextInt();
		int C = in.nextInt();
		int[][] rectangle = new int[R][C];
		for (int x = 0; x < R; x ++) {
			for (int y = 0; y < C; y++ ) {
				rectangle[x][y] = in.nextInt();
			}
		}
		process(rectangle, R, C);
	}
}
