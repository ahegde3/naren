package com.narren.leetCode;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * A binary watch has 4 LEDs on the top which represent the hours (0-11),
and the 6 LEDs on the bottom represent the minutes (0-59).

Each LED represents a zero or one, with the least significant bit on the right.


For example, the above binary watch reads "3:25".

Given a non-negative integer n which represents the number of LEDs that are currently on,
return all possible times the watch could represent.

Example:

Input: n = 1
Return: ["1:00", "2:00", "4:00", "8:00", "0:01", "0:02", "0:04", "0:08", "0:16", "0:32"]
Note:
The order of output does not matter.
The hour must not contain a leading zero, for example "01:00" is not valid, it should be "1:00".
The minute must be consist of two digits and may contain a leading zero, for example "10:2"
is not valid, it should be "10:02".
 * 
 * https://leetcode.com/problems/binary-watch/?tab=Description
 * 
 * @author nsbisht
 *
 */
public class BinaryWatch {

	public static void main(String[] args) {
		System.out.println(new BinaryWatch().readBinaryWatch(2));
	}
	public List<String> readBinaryWatch(int num) {
		ArrayList<String> result = new ArrayList<String>();
		int[] hour = new int[]{1, 2, 4, 8};
		int[] min = new int[]{1, 2, 4, 8, 16, 32};

		for(int i = 0; i <= num; i++) {
			ArrayList<Integer> hours = generateItems(hour, i);
			ArrayList<Integer> mins = generateItems(min, num - i);
			for(int h : hours) {
				if(h >= 12) {
					continue;
				}
				for(int m : mins) {
					if(m > 59) {
						continue;
					}
					result.add(h + ":" + (m >= 10 ? m : "0" + m ));
				}
			}
		}

		return result;
	}

	ArrayList<Integer> generateItems(int[] time, int count) {
		ArrayList<Integer> combination = new ArrayList<>();
		generateItems(time, count, combination, 0, 0);
		return combination;
	}

	void generateItems(int[] time, int count, ArrayList<Integer> list, int index, int sum) {
		if(count == 0) {
			list.add(sum);
			return;
		}
		for(int i = index; i < time.length; i++) {
			generateItems(time, count - 1, list, i + 1, sum + time[i]);
		}
	}

}
