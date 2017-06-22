package com.narren.leetCode;
/**
 * 
Say you have an array for which the ith element is the price of a given stock on day i.

If you were only permitted to complete at most one transaction (ie, buy one and sell one share of the stock), design an algorithm to find the maximum profit.

Example 1:
Input: [7, 1, 5, 3, 6, 4]
Output: 5

max. difference = 6-1 = 5 (not 7-1 = 6, as selling price needs to be larger than buying price)
Example 2:
Input: [7, 6, 4, 3, 1]
Output: 0

In this case, no transaction is done, i.e. max profit = 0.
 * 
https://leetcode.com/problems/best-time-to-buy-and-sell-stock/
 * 
 * @author naren
 *
 */
public class BestTimetoBuyandSellStock {

	public static void main(String[] args) {
		BestTimetoBuyandSellStock bst = new BestTimetoBuyandSellStock();
		System.out.println(bst.maxProfit(new int[]{7, 1, 5, 3, 6, 4, 10, 3, 4, 20, 4}));
	}
	
	public int maxProfit(int[] prices) {
		int maxProfit = 0;
		int min = Integer.MAX_VALUE;
		for(int p : prices) {
			min = Math.min(min, p);
			maxProfit = Math.max(maxProfit, p - min);
		}
		return maxProfit;
	}
}
