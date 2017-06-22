package com.narren.leetCode;
/**
Given a binary tree, find its minimum depth.

The minimum depth is the number of nodes along the shortest path from the root node down to the nearest leaf node.

https://leetcode.com/problems/minimum-depth-of-binary-tree/#/description
 * 
 * @author naren
 *
 */
public class MinimumDepthofBinaryTree {

	int minDepth = Integer.MAX_VALUE;
	public int minDepth(TreeNode root) {
		if(root == null) {
			return 0;
		}
		findMinDepth(root, 1);
		return minDepth;
	}

	void findMinDepth(TreeNode node, int depth) {
		if(node == null) {
			return;
		}
		if((node.left == null && node.right == null)) {
			minDepth = Math.min(minDepth, depth);
			return;
		}
		findMinDepth(node.left, depth + 1);
		findMinDepth(node.right, depth + 1);

	}
}
