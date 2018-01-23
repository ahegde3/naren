package com.narren.gayle.treeAndGraph;

import com.narren.leetCode.TreeNode;

public class isBST {

	/**
	 * If a given tree is BST
	 * @param root
	 * @param min
	 * @param max
	 * @return
	 */
	boolean isBST(TreeNode root, int min, int max) {
		if(root == null) {
			return true;
		}
		if(min >= root.val || root.val > max) {
			return false;
		}
		
		return isBST(root.left, min, root.val) && isBST(root.right, root.val, max);
	}
}
