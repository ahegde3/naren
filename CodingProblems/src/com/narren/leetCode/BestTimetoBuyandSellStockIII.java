package com.narren.leetCode;
/**
 * 
Say you have an array for which the ith element is the price of a given stock on day i.

Design an algorithm to find the maximum profit. You may complete at most two transactions.

Note:
You may not engage in multiple transactions at the same time (ie, you must sell the stock before you buy again).

https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/
 * 
 * @author naren
 *
 */
public class BestTimetoBuyandSellStockIII {

	public static void main(String[] args) {
		System.out.println(new BestTimetoBuyandSellStockIII().maxProfit(new int[]{12, 5}));
	}
	public int maxProfit(int[] prices) {
		if(prices.length < 2) {
			return 0;
		}
		return maxProfit(prices, 2);
	}
	
	int maxProfit(int[] prices, int k) {
		int[][] T = new int[k + 1][prices.length];
		
		for(int i = 1; i <= k; i++) {
			int maxDiff = Integer.MIN_VALUE;
			for(int j = 1; j < prices.length; j++) {
				maxDiff = Math.max(maxDiff, T[i - 1][j - 1] - prices[j - 1]);
				T[i][j] = Math.max(T[i][j - 1], (maxDiff + prices[j]));
			}
		}
		
		return T[k][prices.length - 1];
	}
}
