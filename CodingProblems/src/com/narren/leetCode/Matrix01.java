package com.narren.leetCode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 
Given a matrix consists of 0 and 1, find the distance of the nearest 0 for each cell.

The distance between two adjacent cells is 1.
Example 1: 
Input:

0 0 0
0 1 0
0 0 0
Output:
0 0 0
0 1 0
0 0 0
Example 2: 
Input:

0 0 0
0 1 0
1 1 1
Output:
0 0 0
0 1 0
1 2 1
Note:
The number of elements of the given matrix will not exceed 10,000.
There are at least one 0 in the given matrix.
The cells are adjacent in only four directions: up, down, left and right.
 * 
 * @author naren
 *
 */
public class Matrix01 {
	public static void main(String[] args) {
//		int[][] array = new int[][]{{1, 0, 1, 1, 0, 0, 1, 0, 0, 1}
//		,{0, 1, 1, 0, 1, 0, 1, 0, 1, 1}
//		,{0, 0, 1, 0, 1, 0, 0, 1, 0, 0}
//		,{1, 0, 1, 0, 1, 1, 1, 1, 1, 1}
//		,{0, 1, 0, 1, 1, 0, 0, 0, 0, 1}
//		,{0, 0, 1, 0, 1, 1, 1, 0, 1, 0}
//		,{0, 1, 0, 1, 0, 1, 0, 0, 1, 1}
//		,{1, 0, 0, 0, 1, 1, 1, 1, 0, 1}
//		,{1, 1, 1, 1, 1, 1, 1, 0, 1, 0}
//		,{1, 1, 1, 1, 0, 1, 0, 0, 1, 1}
//		};
		
		int[][] array = new int[][]
				{
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 0, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};
		List<List<Integer>> updatedMatrix = new ArrayList<List<Integer>>();;
		for(int j = 0; j < array.length; j++) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			for(int k = 0; k < array[0].length; k++) {
				list.add(array[j][k]);
			}
			updatedMatrix.add(list);
		}
		
		long milis = System.currentTimeMillis();
		new Matrix01().updateMatrix(updatedMatrix);
		System.out.println(System.currentTimeMillis() - milis);

	}
	
	static class element {
		int i;
		int j;
		int d;
		
		public element(int k, int l, int dis) {
			i = k;
			j = l;
			d = dis;
		}
	}
	
	public List<List<Integer>> updateMatrix(List<List<Integer>> matrix) {
		List<List<Integer>> updatedMatrix;
		Integer[][] array = new Integer[matrix.size()][];

		int i = 0;
		for (List<Integer> nestedList : matrix) {
			array[i++] = nestedList.toArray(new Integer[nestedList.size()]);
		}
		i = matrix.size();
		int[][] memo = new int[array.length][array[0].length];
		updatedMatrix = new ArrayList<List<Integer>>(i);
		
		Queue<element> queue = new LinkedList<element>(); 
		for(int j = 0 ; j < i; j++) {
			for(int k = 0; k < array[0].length; k++) {
				if((array[j][k]) == 0) {
					array[j][k] = 0;
					queue.offer(new element(j, k, 0));
				} else {
					array[j][k] = Integer.MAX_VALUE;
				}
			}
		}
		while(!queue.isEmpty()) {
			element e = queue.poll();
			// left
			int r = e.i;
			int c = e.j - 1;
			
			if(c >= 0 && array[r][c] != 0 && e.d + 1 < array[r][c]) {
				array[r][c] = Math.min(array[r][c], e.d + 1);
				queue.add(new element(r, c, array[r][c]));
			}

			// up
			r = e.i - 1;
			c = e.j;

			if(r >= 0 && array[r][c] != 0 && e.d + 1 < array[r][c]) {
				array[r][c] = Math.min(array[r][c], e.d + 1);
				queue.add(new element(r, c, array[r][c]));
			}

			// right
			r = e.i;
			c = e.j + 1;

			if(c < array[0].length && array[r][c] != 0 && e.d + 1 < array[r][c]) {
				array[r][c] = Math.min(array[r][c], e.d + 1);
				queue.add(new element(r, c, array[r][c]));
			}

			// down
			r = e.i + 1;
			c = e.j;

			if(r < array.length && array[r][c] != 0 && e.d + 1 < array[r][c]) {
				array[r][c] = Math.min(array[r][c], e.d + 1);
				queue.add(new element(r, c, array[r][c]));
			}
		}
		

		for(int j = 0; j < array.length; j++) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			for(int k = 0; k < array[0].length; k++) {
				list.add(array[j][k]);
				//System.out.print(array[j][k]);
				//System.out.print(" ");
			}
			updatedMatrix.add(list);
			//System.out.println();
		}
		return updatedMatrix;
	}
}
