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
		if(map.getOrDefault(newSum, 0) > 0) {
			map.put(newSum, map.get(newSum) - 1);
		} 
		return res;
	}
	
	public static void main(String[] args) {
		TreeNode root = new TreeNode(1);
		TreeNode root2 = new TreeNode(2);
		TreeNode root3 = new TreeNode(3);
		TreeNode root4 = new TreeNode(4);
		TreeNode root5 = new TreeNode(5);
		TreeNode root6 = new TreeNode(6);
		TreeNode root7 = new TreeNode(7);
		TreeNode root8 = new TreeNode(8);
		TreeNode root9 = new TreeNode(9);
		TreeNode root10 = new TreeNode(10);
		TreeNode root11 = new TreeNode(11);
		TreeNode root12 = new TreeNode(12);
		TreeNode root13 = new TreeNode(13);
		TreeNode root14 = new TreeNode(14);
		
		root13.left = root14;
		root12.right = root13;
		root8.right = root12;
		
		root10.left = root11;
		root9.right = root10;
		root8.left = root9;
		
		root.right = root8;
		
		root5.left = root6;
		root5.right = root7;
		root2.right = root5;
		root3.right = root4;
		root2.left = root3;
		
		root.left = root2;
		
		PathSumIII ps = new PathSumIII();
		ps.pathSum(root, 12);
		
	}
}

