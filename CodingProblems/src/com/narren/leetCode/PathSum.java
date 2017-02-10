package com.narren.leetCode;
/**
 * 
Given a binary tree and a sum, determine if the tree has a root-to-leaf path such
that adding up all the values along the path equals the given sum.

For example:
Given the below binary tree and sum = 22,
              5
             / \
            4   8
           /   / \
          11  13  4
         /  \      \
        7    2      1
return true, as there exist a root-to-leaf path 5->4->11->2 which sum is 22.
 *
https://leetcode.com/problems/path-sum/
 * @author naren
 *
 */
public class PathSum {

	public boolean hasPathSum(TreeNode root, int sum) {
		if(root == null) {
			return false;
		}
		return findPathWithSum(root, sum);
	}

	boolean findPathWithSum(TreeNode node, int curSum) {
		if(node == null) {
			return false;
		}
		if(node.left == null && node.right == null) {
			return node.val - curSum == 0;
		}

		return findPathWithSum(node.left, curSum - node.val)
				|| findPathWithSum(node.right, curSum - node.val);
	}

	public static void main(String[] args) {
		TreeNode node = new TreeNode(1);
		TreeNode node2 = new TreeNode(2);
		node2.left = null;
		node2.right = null;
		node.right = null;
		node.left = node2;
		System.out.println(new PathSum().hasPathSum(node, 1));

	}
}

class TreeNode {
	int val;
	TreeNode left;
	TreeNode right;
	TreeNode(int x) { val = x; }
}