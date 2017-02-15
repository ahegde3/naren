package com.narren.leetCode;
/**
 * 
Given a binary tree, find its maximum depth.

The maximum depth is the number of nodes along the longest path from the
root node down to the farthest leaf node.
 *
 * https://leetcode.com/problems/maximum-depth-of-binary-tree/
 * 
 * @author nsbisht
 *
 */
public class MaximumDepthofBinaryTree {

	public int maxDepth(TreeNode root) {
		if(root == null) {
			return 0;
		}
		int initDepth = 1;
		return Math.max(getMaxDepth(root, initDepth), initDepth);
	}

	int getMaxDepth(TreeNode root, int sum) {
		if(root == null) {
			return sum - 1;
		}
		return Math.max(getMaxDepth(root.left, sum + 1), getMaxDepth(root.right, sum + 1));
	}
}
