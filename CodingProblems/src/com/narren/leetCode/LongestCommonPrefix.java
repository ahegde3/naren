package com.narren.leetCode;

public class LongestCommonPrefix {

	public String longestCommonPrefix(String[] strs) {
		int minLen = Integer.MAX_VALUE;
		String minString = "";
		for(String s : strs) {
			if(s.length() < minLen) {
				minLen = s.length();
				minString = s;
			}
		}
		int s = 0;
		int m = (minLen - 1) / 2;
		int e = minLen - 1;
		char[] mainStr = minString.toCharArray();
		for(int i = 0; i < strs.length; i++) {
			char[] tempStr = strs[i].toCharArray();
			for(int k = s; k <= m && k <= e; k++) {
				if(tempStr[k] != mainStr[k]) {
					e = k - 1; 
				}
			}
			if(e != minLen - 1) {
				String lcpStr = "";
				for(int l = 0; l <= e; l++) {
					lcpStr += mainStr[l];
				}
				return lcpStr;
			}

		}

		for(int i = 0; i < strs.length; i++) {
			char[] tempStr = strs[i].toCharArray();
			for(int k = m + 1; k <= e; k++) {
				if(tempStr[k] != mainStr[k]) {
					e = k - 1; 
				}
			}
			if(e != minLen - 1) {
				String lcpStr = "";
				for(int l = 0; l <= e; l++) {
					lcpStr += mainStr[l];
				}
				return lcpStr;
			}

		}

		String lcpStr = "";
		for(char c : mainStr) {
			lcpStr += c;
		}
		return lcpStr;
	}

	public static void main (String[] args) {
		LongestCommonPrefix sol = new LongestCommonPrefix();
		String [] strs = new String[]{"leets", "leety", "leet", "lety"};
		System.out.println(sol.longestCommonPrefix(strs));
	}
}
