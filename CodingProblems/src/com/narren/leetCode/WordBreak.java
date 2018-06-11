package com.narren.leetCode;

import java.util.ArrayList;
import java.util.List;

public class WordBreak {

	public static void main(String[] args) {
		List<String> wordDict = new ArrayList<>();
		wordDict.add("lee");
		wordDict.add("leet");
		wordDict.add("code");
		wordBreak("leetcode", wordDict);
	}
	public static boolean wordBreak(String s, List<String> wordDict) {

		int len = s.length();
		boolean[] dp = new boolean[len+1];
		dp[0] = true;// Base Case

		for(int i=1;i<=len;i++) // Since we need to use subString Method we need to go till length
		{
			for(int j=0;j<i;j++) // For all possible sub string combinations with start as j
			{
				if(dp[i]) // Added for optimization
					break;
				dp[i] = dp[j] && wordDict.contains(s.substring(j,i));  // dp[j] makes sure that the word upto j exists in dixtionary and wordDict.contains(s.substring(j,i)) makes sure the remaining word is part of dictionary
			}
		}


		return dp[len];
	}
}
