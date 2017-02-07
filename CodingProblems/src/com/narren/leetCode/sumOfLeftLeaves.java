package com.narren.leetCode;
/**
Find the sum of all left leaves in a given binary tree.

Example:

    3
   / \
  9  20
    /  \
   15   7

There are two left leaves in the binary tree, with values 9 and 15 respectively. Return 24.

https://leetcode.com/problems/sum-of-left-leaves/
 * 
 * @author nsbisht
 *
 */
public class sumOfLeftLeaves {
	public class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode(int x) {
			val = x;
		}
	}
	public int sumOfLeftLeaves(TreeNode root) {
		return findSum(root, false);
	}

	int findSum(TreeNode node, boolean isLefet) {
		if(node == null) {
			return 0;
		}
		if(node.left == null && node.right == null && isLefet) {
			return node.val;
		}
		return findSum(node.left, true) + findSum(node.right, false);

	}
}
