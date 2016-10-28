package com.narren.tree;

public class IsSymmetricRecursive {

	static Node root;
	static String leftS;
	static String rightS;
	
	
	static boolean isSymmetric(Node left, Node right) {
		if(left == null && right == null) {
			return true;
		}
		if(left != null && right != null) {
			return ((left.data == right.data)
					&& isSymmetric(left.right, right.left)
					&& isSymmetric(left.left, right.right));
		}
		return false;
	}
	
	public static void main(String[] args) {
		IsSymmetricRecursive tree = new IsSymmetricRecursive();
		 
		// False case
//        tree.root = new Node(1);
//        tree.root.left = new Node(2);
//        tree.root.right = new Node(3);
//        tree.root.left.left = new Node(4);
//        tree.root.left.right = new Node(5);
//        tree.root.right.left = new Node(6);
//        tree.root.right.left.right = new Node(7);
		
		// True case
		tree.root = new Node(1);
        tree.root.left = new Node(2);
        tree.root.right = new Node(2);
        tree.root.left.left = new Node(4);
        tree.root.left.right = new Node(3);
        tree.root.right.left = new Node(3);
        tree.root.right.left.right = new Node(5);
        tree.root.right.right = new Node(4);
        tree.root.left.right.left = new Node(5);
        
        System.out.println(tree.isSymmetric(root.left, root.right));
	}
}
