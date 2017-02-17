package com.narren.leetCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 
Given a binary tree, return the bottom-up level order traversal of its nodes' values.
(ie, from left to right, level by level from leaf to root).

For example:
Given binary tree [3,9,20,null,null,15,7],
    3
   / \
  9  20
    /  \
   15   7
return its bottom-up level order traversal as:
[
  [15,7],
  [9,20],
  [3]
]
 * 
 * ps://leetcode.com/problems/binary-tree-level-order-traversal-ii/
 * 
 * @author nsbisht
 *
 */
public class BinaryTreeLevelOrderTraversalII {
	public List<List<Integer>> levelOrder(TreeNode root) {
		List<List<Integer>> list = new ArrayList<>();
		Queue<TreeNode> queue = new LinkedList<>();
		if(root == null) {
			return list;
		}
		queue.offer(root);
		while(!queue.isEmpty()) {
			int elements = queue.size();
			ArrayList<Integer> subList = new ArrayList<Integer>();
			for(int i = 0; i < elements; i++) {
				TreeNode node = queue.poll();
				if(node.left != null) {
					queue.offer(node.left);
				}
				if(node.right != null) {
					queue.offer(node.right);
				}
				subList.add(node.val);
			}
			list.add(subList);
		}

		Collections.reverse(list);
		return list;
	}
}
