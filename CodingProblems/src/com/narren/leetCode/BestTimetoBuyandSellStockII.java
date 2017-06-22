package com.narren.leetCode;
/**
Say you have an array for which the ith element is the price of a given stock on day i.

Design an algorithm to find the maximum profit. You may complete as many transactions as you like
(ie, buy one and sell one share of the stock multiple times). However, you may not engage in multiple transactions at the same time
(ie, you must sell the stock before you buy again).
 * 
 * 
 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/#/description
 * 
 * @author naren
 *
 */
public class BestTimetoBuyandSellStockII {
	public int maxProfit(int[] prices) {
		int curMin = Integer.MAX_VALUE;
		int profit = 0;
		for(int i = 0; i < prices.length; i++) {
			curMin = Math.min(curMin, prices[i]);
			if(prices[i] > curMin) {
				if(i + 1 < prices.length && prices[i] > prices[i + 1]) {
					profit += prices[i] - curMin;
					curMin = Integer.MAX_VALUE;
				} else if(i == prices.length - 1) {
					profit += prices[i] - curMin;
				}
			}
		}
		return profit;
	}
}
