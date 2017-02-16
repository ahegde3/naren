package com.narren.leetCode;
/**
Given a binary tree, check whether it is a mirror of itself (ie, symmetric around its center).

For example, this binary tree [1,2,2,3,4,4,3] is symmetric:

    1
   / \
  2   2
 / \ / \
3  4 4  3
But the following [1,2,2,null,3,null,3] is not:
    1
   / \
  2   2
   \   \
   3    3
Note:
Bonus points if you could solve it both recursively and iteratively.

https://leetcode.com/problems/symmetric-tree/
 * 
 * @author nsbisht
 *
 */
public class SymmetricTree {
	public boolean isSymmetric(TreeNode root) {
		if(root == null) {
			return true;
		}
		return isSymm(root.left, root.right);
	}

	boolean isSymm(TreeNode node1, TreeNode node2) {
		if(node1 == null && node2 == null) {
			return true;
		}
		if(node1 == null || node2 == null) {
			return false;
		}

		return node1.val == node2.val && isSymm(node1.right, node2.left)
				&& isSymm(node1.left, node2.right);
	}
}
