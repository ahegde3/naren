package com.narren.tree;


public class TreeHeightRecursive {
	
	class Node {
		Node left;
		Node right;
		int data;
	}
	
	int height(Node root) {
		if (root == null) {
			return 0;
		}
		int left = height(root.left);
		int right = height(root.right);
		return (left > right ? left : right) + 1;
	}
}
