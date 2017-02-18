package com.narren.leetCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 
Given a binary tree, imagine yourself standing on the right side of it, return the
values of the nodes you can see ordered from top to bottom.

For example:
Given the following binary tree,
   1            <---
 /   \
2     3         <---
 \     \
  5     4       <---
You should return [1, 3, 4].

https://leetcode.com/problems/binary-tree-right-side-view/?tab=Description
 * 
 * @author naren
 *
 */
public class BinaryTreeRightSideView {
	public List<Integer> rightSideView(TreeNode root) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		int[] maxlen = new int[]{0};
		findRightSideView(root, maxlen, 1, list);
		return list;
	}
	
	void findRightSideView(TreeNode node, int[] maxLevel, int level, ArrayList<Integer> list) {
		if(node == null) {
			return;
		}
		if(level > maxLevel[0]) {
			list.add(node.val);
		}
		
		findRightSideView(node.right, maxLevel, level + 1, list);
		maxLevel[0] = Math.max(maxLevel[0], level);
		findRightSideView(node.left, maxLevel, level + 1, list);
	}
}
