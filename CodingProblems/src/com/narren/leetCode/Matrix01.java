package com.narren.leetCode;

import java.util.ArrayList;
import java.util.List;

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
	public List<List<Integer>> updateMatrix(List<List<Integer>> matrix) {
		List<List<Integer>> updatedMatrix;
		Integer[][] array = new Integer[matrix.size()][];

		int i = 0;
		for (List<Integer> nestedList : matrix) {
		    array[i++] = nestedList.toArray(new Integer[nestedList.size()]);
		}
		i = matrix.size();
		updatedMatrix = new ArrayList<List<Integer>>(i);
		for(int j = 0 ; j < i; j++) {
			for(int k = 0; k < array[0].length; k++) {
				if((array[j][k] & 1) == 0) {
					array[j][k] = 0;
				} else {
					int left = -1;
					int right = -1;
					int up = -1;
					int down = -1;
					if(k - 1 >= 0) {
						left = array[j][k - 1] >> 1;
					}
					if(k + 1 < array[0].length) {
						right = array[j][k + 1] >> 1;
					}
					if(j - 1 >= 0) {
						up = array[j - 1][k] >> 1;
					}
					if(j + 1 < i) {
						down = array[j + 1][k] >> 1;
					}
					
					if(left == 0 || right == 0| up == 0 | down == 0) {
						array[j][k] = array[j][k] | 1 << 1;
					} else {
						int min = Integer.MAX_VALUE;
						min = left > 0 ? Math.min(min, left) : min;
						min = right > 0 ? Math.min(min, right) : min;
						min = up > 0 ? Math.min(min, up) : min;
						min = down > 0 ? Math.min(min, down) : min;
						array[j][k] = ((array[j][k] >> 1) + min) | array[j][k];
						
					}
					
				}
				
			}
			
		}
		
		for(int j = 0; j < array.length; j++) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			for(int k = 0; k < array[0].length; k++) {
				list.add(array[j][k] >> 1);
			}
			updatedMatrix.add(list);
		}
		return updatedMatrix;
		/**
		 * Input:
[[0,0,0],[0,1,0],[1,1,1]]
Output:
[[0,0,0],[0,1,0],[1,1,1]]
Expected:
[[0,0,0],[0,1,0],[1,2,1]]
		 */

	}
}
