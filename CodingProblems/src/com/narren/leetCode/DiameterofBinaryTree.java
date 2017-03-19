package com.narren.leetCode;
/**
 * 
Given a binary tree, you need to compute the length of the diameter of the tree. The diameter of a binary tree
is the length of the longest path between any two nodes in a tree. This path may or may not pass through the root.

Example:
Given a binary tree 
          1
         / \
        2   3
       / \     
      4   5    
Return 3, which is the length of the path [4,2,1,3] or [5,2,1,3].

Note: The length of path between two nodes is represented by the number of edges between them.
 * 
 * @author naren
 *
 */
public class DiameterofBinaryTree {

	public int diameterOfBinaryTree(TreeNode root) {
		if(root == null) {
			return 0;
		}
		
		int left = findHeight(root.left);
		int right = findHeight(root.right);
		
		int lC = diameterOfBinaryTree(root.left);
		int rC = diameterOfBinaryTree(root.right);
		
		int r = left + right;
		return rC > lC ? (rC > r ? rC : r) : (lC > r ? lC : r);
	}
	
	int findHeight(TreeNode root) {
		if (root == null) {
			return 0;
		}
		int left = findHeight(root.left);
		int right = findHeight(root.right);
		return (left > right ? left : right) + 1;
	}
	
	
}
