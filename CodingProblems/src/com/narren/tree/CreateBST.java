package com.narren.tree;

/**
 * Give a sorted array, make a BST with the minimal height
 * @author nsbisht
 *
 */
public class CreateBST {

	int[] arr;
	
	Node create(int i, int j) {
		if(j < i) {
			return null;
		}
		int m = i + (j - i)/2;
		
		Node node = new Node(arr[m]);
		node.data = arr[m];
		
		node.left = create(i, m - 1);
		node.right = create(m + 1, j);
		return node;
	}
	
	public static void main(String[] args) {
		CreateBST instance = new CreateBST();
		instance.arr = new int[]{1,2,3,4,5,6,7,8};
		instance.create(0, instance.arr.length - 1);
		System.out.println();
	}
}
