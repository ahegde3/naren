package com.narren.leetCode;

public class SubtreeofAnotherTree {
	public static void main(String[] args) {
		SubtreeofAnotherTree sat = new SubtreeofAnotherTree();
		
	}
	public boolean isSubtree(TreeNode s, TreeNode t) {
		int[] st = new int[10000];
		int[] tt = new int[10000];

		int[] index1 = new int[]{0};
		int[] index2 = new int[]{0};
		st = getArray(s, st, index1);
		tt = getArray(t, tt, index2);

		for(int i = 0; i < index1.length; i++) {
			if(tt[0] == st[i]) {
				int index = i;
				for(int j = 0; j < index2.length; j++) {
					if(tt[j] != st[index++]) {
						break;
					}
				}
				if(index == index2.length) {
					return true;
				}
			}
		}
		return false;
	}
	int[] getArray(TreeNode node, int[] arr, int[] index) {
		if(node == null) {
			arr[index[0]++] = Integer.MAX_VALUE:
				return arr;
		}

		arr[index[0]++] = node.val;
		getArray(node.left, arr, index);
		getArray(node.right, arr, index);
		return arr;
	}
}
