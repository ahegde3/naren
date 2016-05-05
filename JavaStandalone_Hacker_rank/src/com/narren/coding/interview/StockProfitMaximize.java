package com.narren.coding.interview;

/**
 *  Buy and sell stocks for K times for maximum profit
 * @author nsbisht
 *
 */
public class StockProfitMaximize {

    static int noOfStocks = 8;
    static int k = 3;
    static int[][] T = new int[k + 1][noOfStocks];
    //2,5,7,1,4,3,1,3
    static int[] price = new int[]{30,50,30,40,30,41,30,42};

    private static int findMaxProfit() {
        for(int i = 1; i <= k; i++) {
            for(int j = 1; j < noOfStocks; j++) {
                int temp = 0;
                for(int m = 0; m < j; m++) {
                    temp = Math.max(temp, (price[j] - price[m]) + T[i - 1][m]);
                }
                T[i][j] = Math.max(T[i][j - 1], temp);
            }
        }
        return T[k][noOfStocks - 1];
    }
    
    public static void main(String[] args) {
        System.out.println(findMaxProfit());
    }
}
