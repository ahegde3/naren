package com.narren.leetCode;
/**
 * 
You are climbing a stair case. It takes n steps to reach to the top.

Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?

Note: Given n will be a positive integer.
 * 
 * 
https://leetcode.com/problems/climbing-stairs/
 * 
 * @author naren
 *
 */
public class ClimbingStairs {
	public int climbStairs(int n) {
		if(n < 1) {
			return 0 ;
		}
		if(n == 1) {
			return 1 ;
		}
		if(n == 2) {
			return 2 ;
		}
		int sol[] = new int[n + 1];
		sol[1] = 1;
		sol[2] = 2;
		return countWays(sol, n);
	}
	
	int countWays(int[] sol, int n) {
		if(sol[n] > 0) {
			return sol[n];
		}
		
		sol[n] = countWays(sol, n - 1) + countWays(sol, n - 2);
		return sol[n];
	}
}
