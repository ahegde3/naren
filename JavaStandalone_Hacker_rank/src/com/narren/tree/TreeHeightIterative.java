package com.narren.tree;

public class TreeHeightIterative {
	
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
    }
	
	int height = -1;
    Node[] array = new Node[10000];
    
    MyQueue queue = new MyQueue();
	int height(Node root) {

		if (root == null) {
			return height;
		}
		queue.enqueue(root);
		while (true) {
			int size = queue.size();
			if (size == 0) {
				break;
			}
			while (size > 0) {
				Node node = queue.dequeue();
				if (node.left != null) {
					queue.enqueue(node.left);
				}
				if (node.right != null) {
					queue.enqueue(node.right);
				}
				size--;
			}
			height++;
		}
		return height;
	}

}
