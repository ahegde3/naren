package com.narren.leetCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 
Given a list of 24-hour clock time points in "Hour:Minutes" format, find the minimum minutes difference between any two time points in the list.

Example 1:
Input: ["23:59","00:00"]
Output: 1
Note:
The number of time points in the given list is at least 2 and won't exceed 20000.
The input time is legal and ranges from 00:00 to 23:59.

 * @author naren
 *
 */
public class MinimumTimeDifference {
	public static void main(String[] args) {
		ArrayList<String> list = new ArrayList<>();
		list.add("00:00");
		list.add("23:59");
		list.add("00:00");
		// ["00:00","23:59"]
		System.out.println(new MinimumTimeDifference().findMinDifference(list));
	}
	public int findMinDifference(List<String> timePoints) {
		int[] minutes = new int[1441];

		for(String s : timePoints) {
			String[] res = s.split(":");
			int h = Integer.parseInt(res[0]);
			int m = Integer.parseInt(res[1]);
			minutes[m + (h * 60)] += 1;

		}

		int result = Integer.MAX_VALUE;
		boolean foundb = false;
		int found = -1;
		int firstIndex = -1;
		for(int j = 0; j < 1441; j++) {
			if(minutes[j] > 1) {
				return 0;
			}
			if(minutes[j] > 0) {
				if(firstIndex > -1) {
					result = Math.min(result, j - firstIndex);
					firstIndex = j;
				} else if(firstIndex < 0) {
					if(!foundb) {
						found = j;
						foundb = true;
					}
					firstIndex = j;
				}
			}

		}
		result = Math.min(result, (1440 - firstIndex) + found);
		return result;
	}
}
