package com.narren.tree;

public class IsBST {

	class Node {
		Node left;
		Node right;
		int data;
	}

	boolean isBst(Node root) {
		return checkBst(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	boolean checkBst(Node root, int min, int max) {

		if (root == null) {
			return true;
		}
		if (root.data <= min || root.data > max) {
			return false;
		}
		return checkBst(root.left, min, root.data) && checkBst(root.right, root.data, max);
	}
}
