package com.narren.hackerRank;

public class BinarySearchTreeLowestCommonAncestor {

	int height = -1;
	Node[] array = new Node[10000];
	
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
	
//	static Node lca(Node root,int v1,int v2)
//	{
//	MyQueue queue = new MyQueue();
//	queue.enqueue(root);
//	boolean visited = false;
//	while (!queue.isEmpty()) {
//		Node node = queue.dequeue();
//		if ((node.left != null && node.left.data == ) || (node.right != null) ) {
//			
//		}
//	}
//	
//	}
	private void dfs(Node root, int goal) {
		MyQueue queue = new MyQueue();
		queue.enqueue(root);
		while (!queue.isEmpty()) {
			Node node = queue.peek();
			if (node.left != null) {
				
				queue.enqueue(node.left);
			}
			if (node.left == null) {
				if (node.right != null) {
					queue.enqueue(node.right);
				}
			}
		}
	}
	
}
