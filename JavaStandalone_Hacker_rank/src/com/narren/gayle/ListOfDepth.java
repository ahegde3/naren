package com.narren.gayle;


public class ListOfDepth {

	MyQueue queue = new MyQueue();

	Node[] array = new Node[10000];

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
		int size() {
			return rear - front;
		}
	}

	class Node {
		Node left;
		Node right;
		int data;
	}
	void print () {
		int size = queue.size();
		if (size < 1) {
			return;
		}
		while (size > 0) {
			Node node = queue.dequeue();
			System.out.println(node.data);
			queue.enqueue(node.left);
			queue.enqueue(node.right);
			size--;
		}
		print();
	}
}
