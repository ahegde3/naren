package com.narren.hackerRank;

public class BinarySearchTreeLowestCommonAncestor {

	int height = -1;
	Node[] array = new Node[10000];
	static Node[] array1 = new Node[10000];
	static Node[] array2 = new Node[10000];;
	static boolean firstFound;
	static boolean secondFound;
	static int counter;
	
	
	class Node {
		Node left;
		Node right;
		int data;
	}

	class MyQueue {
		int front;
		int rear;

		void enqueue (Node i)  {
			if (front == 0) {
				front++;
			}
			rear++;
			array[rear] = i;

		}

		Node dequeue() {
			Node res = null;
			if (front > 0) {
				res = array[front];
				if (front == rear) {
					front = 0;
					rear = 0;
				} else {
					front++;
				}
			} else {
				return null;
			}
			return res;
		}

		boolean isEmpty() {
			return front == 0 && rear == 0;
		}

		int size() {
			if (front == 0 && rear == 0) {
				return 0;
			} else {
				return (rear - front) + 1;
			}
		}
		
		Node peek() {
			Node res = null;
			if (front > 0) {
				res = array[front];
			} else {
				return null;
			}
			return res;
		}
	}
	
	static Node lca(Node root,int v1,int v2)
	{
		dfs(root, v1, v2);
		int len = Math.min(array1.length, array2.length);
		for (int i = 0; i < len; i++) {
			if (array1[i] != array2[i]) {
				return array1[i - 1];
			}
		}
		return null;
	}
	
	
	static void dfs(Node root,int v1,int v2) {
		if (root == null) {
			return;
		}
		if (!firstFound) {
			array1[counter] = root;
		}
		if(!secondFound) {
			array2[counter] = root;
		}
		counter++;
		if (root.data == v1) {
			firstFound = true;
		}
		if (root.data == v2) {
			secondFound = true;
		}
		dfs(root.left, v1, v2);
		dfs(root.right, v1, v2);
		counter--;
	}
	
}
