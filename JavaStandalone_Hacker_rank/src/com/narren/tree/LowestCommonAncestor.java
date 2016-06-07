package com.narren.tree;

public class LowestCommonAncestor {

	private Node findLCA(Node p, Node q, Node root) {
		if(root == null) {
			return null;
		}
		if(root.data == p.data) {
			return root;
		}
		if(root.data == q.data) {
			return root;
		}
		
		Node leftNode = findLCA(p, q, root.left);
		Node rightNode = findLCA(p, q, root.right);
		
		if(leftNode != null && rightNode != null) {
			return root;
		}
		
		if(leftNode != null) {
			return leftNode;
		}
		
		return rightNode;
	}
}
