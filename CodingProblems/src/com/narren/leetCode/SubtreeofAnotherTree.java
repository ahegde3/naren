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
		int[] st = new int[10000];
		int[] tt = new int[10000];

		int[] index1 = new int[]{0};
		int[] index2 = new int[]{0};
		st = getArray(s, st, index1);
		tt = getArray(t, tt, index2);

		for(int i = 0; i < index1[0]; i++) {
			if(tt[0] == st[i]) {
				int index = i;
				for(int j = 0; j < index2[0]; j++) {
					if(tt[j] != st[index++]) {
						break;
					}
				}
				if(index -1  == index2[0]) {
					return true;
				}
			}
		}
		return false;
	}
	int[] getArray(TreeNode node, int[] arr, int[] index) {
		if(node == null) {
			arr[index[0]++] = Integer.MAX_VALUE;
				return arr;
		}

		arr[index[0]++] = node.val;
		getArray(node.left, arr, index);
		getArray(node.right, arr, index);
		return arr;
	}
}
