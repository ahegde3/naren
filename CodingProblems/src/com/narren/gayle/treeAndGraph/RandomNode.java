package com.narren.gayle.treeAndGraph;

import com.narren.leetCode.TreeNode;

public class RandomNode {

	TreeNode root = null;
	TreeNode leaf = null;
	
	void insert(TreeNode node) {
		if(root == null) {
			root = node;
			leaf = root;
			return;
		}
		
	}
}
