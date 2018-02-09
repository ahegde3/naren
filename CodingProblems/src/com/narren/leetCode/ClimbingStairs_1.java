package com.narren.leetCode;

public class ClimbingStairs_1 {

    public int climbStairs(int n) {
        return noOfWays(n, 0);
    }
    
    int noOfWays(int n, int curStep) {
        if(curStep > n) {
            return 0;
        }
        
        if(curStep == n) {
            return 1;
        }
        
        int one = noOfWays(n, curStep + 1);
        int two = noOfWays(n, curStep + 2);
        
        return one + two;
    }
    
}
