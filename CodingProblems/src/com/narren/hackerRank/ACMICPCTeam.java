package com.narren.hackerRank;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class ACMICPCTeam {
	   static BufferedReader in = new BufferedReader(new InputStreamReader(
		         System.in));
		   static StringBuilder out = new StringBuilder();

    public static void main(String[] args) throws NumberFormatException, IOException {
    	Scanner sc = new Scanner(System.in);
    	int N = sc.nextInt();
    	int M = sc.nextInt();
    	int maxValue = (int) Math.pow(2, 5) - 1;
    	int[] array = new int[N];
    	int first = 0;
    	int second = 0;
    	for (int i = 0; i < N; i++) {
    		array[i] = Integer.parseInt(sc.next(), 2);
    	}
    	for (int i = 0; i < array.length; i++) {
    		for (int j = i+1; j < array.length; j++) {
    			int or = (array[i] | array[j]);
    			if (or > first) {
    				first = or;
    				
    			}
    		}
    	}
    	System.out.println(first + " " + second);
    }
}
