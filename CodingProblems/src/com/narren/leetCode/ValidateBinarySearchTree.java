package com.narren.leetCode;

import java.util.ArrayList;
import java.util.List;

public class ValidateBinarySearchTree {

	public boolean isValidBST(TreeNode root) {
		ArrayList<Integer> list = new ArrayList<Integer>();

		inOrder(root, list);

		for(int i = 1; i < list.size(); i++) {
			if(list.get(i) <= list.get(i - 1)) {
				return false;
			}
		}

		return true;
	}

	void inOrder(TreeNode root, ArrayList<Integer> list) {
		if(root == null) {
			return;
		}
		inOrder(root.left, list);
		list.add(root.val);
		inOrder(root.right, list);
	}
}
