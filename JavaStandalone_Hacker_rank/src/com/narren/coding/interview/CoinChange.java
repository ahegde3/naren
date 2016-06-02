package com.narren.coding.interview;

/**
 * Find minimum no. of coins to make a total
 * E.g. Given coins of denomination 2,5,6,7
 * We have to make 13 with minimum coins.
 * It will take a minimum of two coins, 6 & 7
 * @author nsbisht
 *
 */
public class CoinChange {

	int[] coins;
	int[] T;
	
	int process(int i, int j) {
		T[i] = Math.min(T[i], T[i - coins[j]] == Integer.MAX_VALUE ? Integer.MAX_VALUE : 1 + T[i - coins[j]]);
		return T[i];
	}
	
	void coinChange(int total, int[] coins) {
		T = new int[total + 1];
		for(int i = 1; i <= total; i++) {
			T[i] = Integer.MAX_VALUE;
		}
		
		for(int j = 0; j < coins.length; j++) {
			for(int i = 1; i <= total; i++) {
				if(i < coins[j]) {
					continue;
				}
				process(i, j);
			}
		}
		
	}
	
	public static void main(String[] args) {
		CoinChange coinChange = new CoinChange();
		coinChange.coins = new int[4];
		coinChange.coins[0] = 2;
		coinChange.coins[1] = 5;
		coinChange.coins[2] = 6;
		coinChange.coins[3] = 7;
		coinChange.coinChange(13, coinChange.coins);
		System.out.println(coinChange.T[13]);
	}
}
