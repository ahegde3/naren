package com.narren.leetCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 
Given numRows, generate the first numRows of Pascal's triangle.

For example, given numRows = 5,
Return

[
     [1],
    [1,1],
   [1,2,1],
  [1,3,3,1],
 [1,4,6,4,1]
]

 https://leetcode.com/problems/pascals-triangle/
 * 
 * @author naren
 *
 */
public class PascalsTriangle {

	public static void main(String[] args) {
		new PascalsTriangle().generate(3);
	}
	public List<List<Integer>> generate(int numRows) {
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		int count = 1;
		if(numRows < 1) {
			return list;
		}
		ArrayList<Integer> array = new ArrayList<Integer>(count);
		array.add(1);
		list.add(array);
		
		while(count < numRows) {
			List<Integer> prevArray = list.get(count - 1); 
			ArrayList<Integer> array1 = new ArrayList<Integer>(count + 1);
			array1.add(0, 1);
			
			int loopCount = count - 1;
			int index = 1;
			while(index <= loopCount) {
				array1.add(index, prevArray.get(index - 1) + prevArray.get(index));
				index++;
			}
			array1.add(count, 1);
			list.add(array1);
			count++;
		}
		
		return list;
	}
}
