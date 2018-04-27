package com.narren.leetCode;

public class SubtreeofAnotherTree {
	public static void main(String[] args) {
		TreeNode s = new TreeNode(3);
		TreeNode s1 = new TreeNode(4);
		TreeNode s2 = new TreeNode(5);
		TreeNode s3 = new TreeNode(1);
		TreeNode s4 = new TreeNode(2);
		TreeNode s5 = new TreeNode(0);
		//		s.left = s1;
		//		s.right = s2;
		//		s1.left = s3;
		//		s1.right = s4;
		//		s4.left = s5;

		TreeNode t = new TreeNode(3);
		TreeNode t1 = new TreeNode(1);
		TreeNode t2 = new TreeNode(2);
		//		t.left = t1;
		//		t.right = t2;

		SubtreeofAnotherTree sat = new SubtreeofAnotherTree();
		System.out.println(sat.isSubtree(s, t));

	}
	public boolean isSubtree(TreeNode s, TreeNode t) {
		return traverse(s, t);
	}
	boolean traverse(TreeNode r, TreeNode l) {
		return (r != null) && (equals(r, l) || traverse(r.left, l) || traverse(r.right, l));
	}

	boolean equals(TreeNode r, TreeNode l) {
		if(r == null && l == null) {
			return true;
		}
		if(r == null || l == null) {
			return false;
		}

		return r.val == l.val && equals(r.left, l.left) && equals(r.right, l.right);
	}
}
