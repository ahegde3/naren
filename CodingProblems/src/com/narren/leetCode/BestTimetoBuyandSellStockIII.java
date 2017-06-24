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
		System.out.println(new BestTimetoBuyandSellStockIII().maxProfit(new int[]{1,2,4,2,5,7,2,4,9,0}));
	}
	public int maxProfit(int[] prices) {
		if(prices.length < 2) {
			return 0;
		}
		return getProfit(prices, 2);
	}

	int getProfit(int[] prices, int k) {
		/**
		 * Formula
		 * p[t][i] = max {p[t][i - 1] (no transaction),
		 *
		 *                maxDiff + prices[j - 1], where maxDiff = max(maxDiff, profit[i - 1][j - 1] - prices[j - 2])
		 *
		 *               }
		 */
		int[][] profit = new int[k + 1][prices.length + 1];

		for(int i = 1; i <= k; i++) {
			int maxDiff = -prices[0]; 
			for(int j = 2; j < prices.length + 1; j++) {
				maxDiff = Math.max(maxDiff, profit[i - 1][j - 1] - prices[j - 2]);
				int max1 = 0;
				profit[i][j] = Math.max(profit[i][j - 1], maxDiff + prices[j - 1]);
			}
		}
		return profit[k][prices.length];
	}

}
