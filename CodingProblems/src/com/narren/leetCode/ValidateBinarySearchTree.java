package com.narren.leetCode;

import java.util.ArrayList;
import java.util.List;

public class ValidateBinarySearchTree {

	public boolean isValidBST(TreeNode root) {
		ArrayList<Integer> list = new ArrayList<Integer>();

		return inOrder(root, list) >= 0 ? true : false;
	}

	int inOrder(TreeNode root, ArrayList<Integer> list) {
		if(root == null) {
			return = 0;
		}
		int l = inOrder(root.left, list);
		if(l == -1) {
			return -1;
		}
		if(list.size() > 0) {
            int lastElement = list.get(list.size() - 1);
		    if(lastElement >= root.val) {
                return -1;
		    }
		}
		
		list.add(root.val);
		
		int r = inOrder(root.right, list);
		if(r == -1) {
			return -1;
		}

        return 0;
	}
}
