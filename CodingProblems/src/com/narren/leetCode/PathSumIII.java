package com.narren.leetCode;

import java.util.HashMap;

/**
 * 
You are given a binary tree in which each node contains an integer value.

Find the number of paths that sum to a given value.

The path does not need to start or end at the root or a leaf, but it must
go downwards (traveling only from parent nodes to child nodes).

The tree has no more than 1,000 nodes and the values are in the range -1,000,000 to 1,000,000.

Example:

root = [10,5,-3,3,2,null,11,3,-2,null,1], sum = 8

      10
     /  \
    5   -3
   / \    \
  3   2   11
 / \   \
3  -2   1

Return 3. The paths that sum to 8 are:

1.  5 -> 3
2.  5 -> 2 -> 1
3. -3 -> 11
 * 
 * 
https://leetcode.com/problems/path-sum-iii/
 * 
 * @author nsbisht
 *
 */
public class PathSumIII {

	public int pathSum(TreeNode root, int sum) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(0, 1);
		return calculatePathSum(root, sum, 0, map);
	}

	int calculatePathSum(TreeNode root, int desired, int curSum, HashMap<Integer, Integer> map) {
		if(root == null) {
			return 0;
		}
		int newSum = root.val + curSum;
		int res = map.getOrDefault(newSum - desired, 0);
		map.put(newSum, map.getOrDefault(newSum, 0) + 1);
		int left = calculatePathSum(root.left, desired, newSum, map);
		int right = calculatePathSum(root.right, desired, newSum, map);
		res += left + right;
		map.remove(newSum);
		return res;
	}
}

