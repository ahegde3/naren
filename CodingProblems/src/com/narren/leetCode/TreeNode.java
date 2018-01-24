package com.narren.leetCode;

public class TreeNode {
	public int val;
	public TreeNode left;
	public TreeNode right;
	public TreeNode(int x) { val = x; }
	
	
	@Override
	public boolean equals(Object obj) {
		return this.val == ((TreeNode)obj).val;
	}
	
	
}
