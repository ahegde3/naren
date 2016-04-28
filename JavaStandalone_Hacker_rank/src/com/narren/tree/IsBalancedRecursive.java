package com.narren.tree;

public class IsBalancedRecursive {

	static Node root;
	static boolean isBalanced = true;
	
	boolean isBalance(Node node) {
		if(!isBalanced) {
			return false;
		}
		if(node == null) {
			return true;
		}
		if(Math.abs(getHeight(node.left) - getHeight(node.right)) > 1) {
			isBalanced = false;
		}
		isBalance(node.left);
		isBalance(node.right);
		return isBalanced;
	}
	
	int getHeight(Node root) {
		if(root == null) {
			return 0;
		}
		
		int leftHeight = getHeight(root.left);
		int rightHeight = getHeight(root.right);
		
		return (leftHeight > rightHeight ? leftHeight : rightHeight) + 1;
	}
	
	public static void main(String[] args) {
		IsBalancedRecursive tree = new IsBalancedRecursive();
		 
        tree.root = new Node(1);
        tree.root.left = new Node(2);
        tree.root.right = new Node(3);
        tree.root.left.left = new Node(4);
        tree.root.left.right = new Node(5);
        tree.root.right.left = new Node(6);
        tree.root.right.left.right = new Node(7);
        
        System.out.println(tree.isBalance(root));
	}
}
