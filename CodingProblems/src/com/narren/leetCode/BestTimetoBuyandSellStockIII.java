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
		/**
		 * Formula
		 * p[t][i] = max {p[t][i - 1] (no transaction),
		 *                 
		 *                max(p[i] - p[j] + p[t - 1][j]), where 0 <= j < i  
		 *                            
		 *               }
		 */
	}
	
}
