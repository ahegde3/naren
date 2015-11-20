package com.narren.graph;

import java.util.Scanner;

public class BfsRevisited {

	/*
	 * 
5
7 6
3 1
1 5
1 2
2 4
2 6
6 7
2
7 7
3 1
1 5
1 2
2 4
2 6
6 7
2 7
3
4 2
1 2
1 3
1
7 8
3 1
1 5
1 2
2 4
2 6
6 7
2 7
3 5
5
5 9
1 2
1 3
1 4
1 5
2 3
2 5
3 4
4 5
5 3
2
	 */
	static class Node {
		int id;
		Node next;
	}

	static Node[] queue;

	static int front = 0;
	static int rear = 0;
	static boolean[] visited;
	static int level[];

	static Node addToAdjanceyList(Node node, int id) {
		Node temp = new Node();
		temp.id = id;
		temp.next = node;
		return temp;
	}

	static Node[] adjanceyList;

	static class Queue {		
		static void enqueue(Node node) {
			System.out.println("Enqueue " + node.id + " rear=" + rear + " front=" + front);
			if (isQueueEmpty()) {
				rear++;
				front++;
			} else {
				front++;
			}
			queue[front] = node;
		}

		static Node dequeue() {
			Node temp = queue[rear];
			System.out.println("  Dequeue " + temp.id + " rear=" + rear + " front=" + front);
			queue[rear] = null;
			if (rear == front) {
				rear = 0;
				front = 0;
			} else {
				rear++;
			}
			return temp;
		}
		
		static boolean isQueueEmpty() {
			return rear == 0 && front == 0;
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		
		while (T > 0) {
			int N = sc.nextInt();
			int E = sc.nextInt();
			adjanceyList = new Node[N + 1];
			queue = new Node[2 * E];
			visited = new boolean[N + 1];
			level = new int[N + 1];
			front = 0;
			rear = 0;

			while (E > 0) {
				int n1 = sc.nextInt();
				int n2 = sc.nextInt();
				adjanceyList[n1] = addToAdjanceyList(adjanceyList[n1], n2);
				adjanceyList[n2] = addToAdjanceyList(adjanceyList[n2], n1);
				E--;
			}
			int s = sc.nextInt();
			process(s);
			T--;
		}
		
	}
	
	static void enqueue (Node node, int parLevel) {
		while (node != null) {
			if (!visited[node.id]) {
				if (level[node.id] > 0 && level[node.id] <= parLevel + 1) {
					//continue;
				} else {
					Queue.enqueue(node);
					level[node.id] = parLevel + 1;
				}

			}
			node = node.next;
		}
	}
	
	static void process(int start) {

		level[start] = 0;
		Node node = adjanceyList[start];
		visited[start] = true;
		enqueue(node, level[start]);
		while(!Queue.isQueueEmpty()) {
			Node temp = Queue.dequeue();
			if (temp != null) {
				visited[temp.id] = true;
				enqueue(adjanceyList[temp.id], level[temp.id]);				
			}
		}
		for (int i = 1; i < level.length; i ++) {
			if (start == i) {
				continue;
			} 
			int res = level[i];
			System.out.print(res > 0 ? 6*res : -1);
			System.out.print(" ");
		}
		System.out.println();
	}
}
