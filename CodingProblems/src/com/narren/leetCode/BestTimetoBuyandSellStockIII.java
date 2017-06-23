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
	        return getProfit(prices, 2);
	    }

	    int getProfit(int[] prices, int k) {
	        /**
	         * Formula
	         * p[t][i] = max {p[t][i - 1] (no transaction),
	         *
	         *                max(p[i] - p[j] + p[t - 1][j]), where 0 <= j < i
	         *
	         *               }
	         */
	        int[][] profit = new int[k + 1][prices.length + 1];

	        for(int i = 1; i <= k; i++) {
	            for(int j = 1; j < prices.length + 1; j++) {
	                int max1 = 0;
	                for(int l = 1; l < j; l++) {
	                    max1 = Math.max(max1, prices[j - 1] - prices[l - 1] + profit[i - 1][l]);
	                }
	                profit[i][j] = Math.max(max1, profit[i][j - 1]);
	            }
	        }
	        return profit[k][prices.length];
	    }
	
}
