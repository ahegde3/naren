package com.narren.leetCode;

import java.util.ArrayList;
import java.util.List;

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
