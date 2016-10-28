package com.narren.tree;

public class Queue {
	Node[] queue;

	int front = 0;
	int rear = 0;

	public Queue(int qSize) {
		queue = new Node[qSize];
	}
	void enqueue(Node node) {
		System.out.println("Enqueue " + node.data + " rear=" + rear + " front=" + front);
		if (isQueueEmpty()) {
			rear++;
			front++;
		} else {
			front++;
		}
		queue[front] = node;
	}

	Node dequeue() {
		Node temp = queue[rear];
		System.out.println("  Dequeue " + temp.data + " rear=" + rear + " front=" + front);
		queue[rear] = null;
		if (rear == front) {
			rear = 0;
			front = 0;
		} else {
			rear++;
		}
		return temp;
	}

	boolean isQueueEmpty() {
		return rear == 0 && front == 0;
	}
}
