package com.narren.coding.practice;

public class ReplaceSpace {
	
	public static void main(String[] args) {
		String str ="Mr john smith    ";
		String finalStr = fillSpace(str, 13);
		System.out.println(finalStr);
		
	}
	
	
	private static String fillSpace(String str, int originalLen) {
		int maxLen = str.length();
		int origLen = originalLen;
		char arr[] = str.toCharArray();
		int diff = maxLen - origLen;
		for(int i = origLen-1; i > 0 ; i--) {
			if(diff == 0) {
				return arr.toString();
			}
			if(arr[i] != ' ') {
				arr[i + diff] = arr[i];
			}else {
				i = i + diff;
				arr[i] = '0';
				arr[i--] = '2';
				arr[i--] = '%';
				diff = diff - 2;
				i = i - diff;
			}
		}
		
		return arr.toString();
	}

}
