package com.narren.leetCode;

import java.util.Arrays;

/**
 * 
Winter is coming! Your first job during the contest is to design a standard heater with fixed warm radius to warm all the houses.

Now, you are given positions of houses and heaters on a horizontal line, find out minimum radius of heaters so that all houses could be covered by those heaters.

So, your input will be the positions of houses and heaters seperately, and your expected output will be the minimum radius standard of heaters.

Note:
Numbers of houses and heaters you are given are non-negative and will not exceed 25000.
Positions of houses and heaters you are given are non-negative and will not exceed 10^9.
As long as a house is in the heaters' warm radius range, it can be warmed.
All the heaters follow your radius standard and the warm radius will the same.
Example 1:
Input: [1,2,3],[2]
Output: 1
Explanation: The only heater was placed in the position 2, and if we use the radius 1 standard, then all the houses can be warmed.
Example 2:
Input: [1,2,3,4],[1,4]
Output: 1
Explanation: The two heater was placed in the position 1 and 4. We need to use radius 1 standard, then all the houses can be warmed.
 * 
 * @author naren
 *
 */
public class Heaters {
	 
	public static void main(String[] args) {
		System.out.println(new Heaters().findRadius(new int[]{1,2,3,4}, new int[]{1,4}));
	}
    int getMin(int house, int[] heaters, int s, int e) {
        if(s < e) {
            int m = (s + e) >>> 1;
            if(house == heaters[m] || house == heaters[s] || house == heaters[e]) {
                return 0;
            }
            if(house > heaters[m]) {
                return getMin(house, heaters, m + 1, e);
            } else {
                return getMin(house, heaters, s, m - 1);
            }
        }
        return Math.min(Math.abs(house - heaters[s]), Math.abs(house - heaters[e]));
    }
    public int findRadius(int[] houses, int[] heaters) {
        Arrays.sort(heaters);
        int min = -1;
        for(int house : houses) {
            min = Math.max(min, getMin(house, heaters, 0, heaters.length - 1));
        }
        return min;
    }
}
