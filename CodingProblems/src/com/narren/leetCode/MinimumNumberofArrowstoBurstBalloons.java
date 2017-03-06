package com.narren.leetCode;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 
There are a number of spherical balloons spread in two-dimensional space. For each balloon, provided input is the start and end
coordinates of the horizontal diameter. Since it's horizontal, y-coordinates don't matter and hence the x-coordinates of start
and end of the diameter suffice. Start is always smaller than end. There will be at most 104 balloons.

An arrow can be shot up exactly vertically from different points along the x-axis. A balloon with xstart and xend bursts by an arrow
shot at x if xstart ≤ x ≤ xend. There is no limit to the number of arrows that can be shot. An arrow once shot keeps travelling up
infinitely. The problem is to find the minimum number of arrows that must be shot to burst all balloons.

Example:

Input:
[[10,16], [2,8], [1,6], [7,12]]

Output:
2

Explanation:
One way is to shoot one arrow for example at x = 6 (bursting the balloons [2,8] and [1,6]) and another arrow at x = 11 (bursting the other two balloons).
Subscribe to see which companies asked this question.
 * 
 * @author naren
 *
 */
public class MinimumNumberofArrowstoBurstBalloons {
	public int findMinArrowShots(int[][] points) {
		if(points == null || points.length < 1 || points[0] == null || points[0].length < 1) {
            return 0;
        }
        if(points[0].length == 1) {
            return 1;
        }
        Arrays.sort(points, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return Integer.compare(o2[0], o1[0]);
            }
        });
        int noOfArrows = 1;
        for(int i = 0; i < points.length; i++) {
            if(i + 1 < points.length) {
                if(points[i + 1][0] >= points[i][0] &&
                        points[i + 1][0] <= points[i][1]) {
                    points[i + 1][1] = Math.min(points[i][1], points[i + 1][1]);
                } else {
                    noOfArrows++;
                }
            }
        }
        return noOfArrows;
	}
}
