package com.narren.gayle.treeAndGraph;

import com.narren.leetCode.TreeNode;

public class FirstCommonAncestor {
	
	TreeNode getFirstCommomAncestor(TreeNode root, TreeNode fNode, TreeNode sNode) {
		
		if(root == null) {
			return null;
		}
		if(root == fNode || root == sNode) {
			return root;
		}
		
		TreeNode left = getFirstCommomAncestor(root.left, fNode, sNode);
		TreeNode right = getFirstCommomAncestor(root.right, fNode, sNode);
		
		
		if(left != null && right != null) {
			return root;
		}
		
		if(left != null) {
			return left;
		}
		
		if(right != null) {
			return right;
		}
		
		return null;
				
	}
	
	public static void main(String[] args) {
		TreeNode root = new TreeNode(1);
		TreeNode root2 = new TreeNode(2);
		TreeNode root3 = new TreeNode(3);
		TreeNode root4 = new TreeNode(4);
		TreeNode root5 = new TreeNode(5);
		TreeNode root6 = new TreeNode(6);
		TreeNode root7 = new TreeNode(7);
		TreeNode root8 = new TreeNode(8);
		TreeNode root9 = new TreeNode(9);
		TreeNode root10 = new TreeNode(10);
		TreeNode root11 = new TreeNode(11);
		TreeNode root12 = new TreeNode(12);
		TreeNode root13 = new TreeNode(13);
		TreeNode root14 = new TreeNode(14);
		
		root13.left = root14;
		root12.right = root13;
		root8.right = root12;
		
		root10.left = root11;
		root9.right = root10;
		root8.left = root9;
		
		root.right = root8;
		
		root5.left = root6;
		root5.right = root7;
		root2.right = root5;
		root3.right = root4;
		root2.left = root3;
		
		root.left = root2;
		
		
		FirstCommonAncestor lca = new FirstCommonAncestor();
		TreeNode node = lca.getFirstCommomAncestor(root, root4, root3);
		
		System.out.println(node.val);
		
		
	}
	
}
