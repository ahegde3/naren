package com.narren.leetCode;

public class StringPermutation {

	static int[] resultArr;
	
	public static void main(String[] args) {
		printPermutation("abc");
	}
	static void printPermutation(String input) {
		resultArr = new int[input.length()];
		int[] inStr = new int[123];
		int uniqueChars = 0;
		for(char c : input.toCharArray()) {
			if(inStr[c] == 0) {
				uniqueChars++;
			}
			inStr[c] += 1;
		}
		int[] charArr = new int[uniqueChars];
		int index = 0;
		for(int i = 0; i < 123; i++) {
			if(inStr[i] > 0) {
				charArr[index++] = i;
			}
		}
		process(0, inStr, charArr);
		
	}
	
	static void process(int level, int[] inStr, int[] charArr) {
		for(int n : charArr) {
			if(inStr[n] > 0) {
				resultArr[level] = n;
				inStr[n] -= 1;
				process(level + 1, inStr, charArr);
			}
		}
		for(int n : resultArr) {
			System.out.print("" + n);
		}
		System.out.println();
	}
}
