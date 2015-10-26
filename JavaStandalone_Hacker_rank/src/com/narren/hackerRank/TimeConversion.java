package com.narren.hackerRank;

import java.util.Scanner;

public class TimeConversion {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String time = sc.next();
		String res = "";
		String[] pm = time.split(":");
		Integer hour = Integer.parseInt(pm[0]);
		if (time.contains("AM")) {
			if (hour != 12) {
				res = time.substring(0, 8);	
			} else {
				res = "00:" + pm[1] + ":" + pm[2].substring(0, 2);
			}
			
		} else {
			if (hour != 12) {
				hour += 12;	
			}
			
			res = hour + ":" + pm[1] + ":" + pm[2].substring(0, 2);
			
		}
		System.out.println(res);
	}
}
