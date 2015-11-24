package com.narren.hackerRank;

import java.util.Scanner;

public class RustMurderer {

	static class Node {
		int id;
		Node next;
	}

	static Node[] queue;
	static Node[] adjancyList;
	static int front;
	static int rear;
	static int[] level;
	static boolean[] visited;

	static void preProcess(int[][] array, int N, int M) {
		for(int i = 1; i <= N; i++) {
			for (int j = 1; j <= N; j++) {
				if (i != j && array[i][j] == 0) {
					adjancyList[i] = addToList(adjancyList[i], j);
				}
			}
		}
	}

	static void enqueue (Node temp, int lev) {
		while (temp != null) {
			if (!visited[temp.id] || (level[temp.id] > 0 && level[temp.id] > lev + 1)) {
				visited[temp.id] = true;
				level[temp.id] = lev + 1;
				Queue.enqueue(temp);
			}
			temp = temp.next;
		}
	}

	static void process(int start) {
		int lev = 0;
		level[start] = 0;
		visited[start] = true;
		Node temp = adjancyList[start];
		enqueue(temp, lev);
		Node node = Queue.dequeue();
		while (node != null) {
			node = adjancyList[node.id];
			enqueue(node, lev + 1);
			node = node.next;

		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while (T > 0) {
			int N = sc.nextInt();
			int M = sc.nextInt();
			queue = new Node[2 * N];
			adjancyList = new Node[N + 1];
			level = new int[N + 1];
			visited = new boolean[N + 1];
			int[][] array = new int[N + 1][N + 1];
			while (M > 0) {
				int n1 = sc.nextInt();
				int n2 = sc.nextInt();
				array[n1][n2] = 1;
				array[n2][n1] = 1;
				M--;
			}
			preProcess(array, N, M);
			int s = sc.nextInt();
			process(s);
			T--;
		}
	}


	static Node addToList(Node node, int id) {
		Node temp = new Node();
		temp.next = node;
		temp.id = id;
		return temp;
	}
	static class Queue {

		static boolean isEmpty() {
			return front == 0 && rear == 0;	
		}

		static void enqueue(Node node) {
			if (isEmpty()) {
				front++;
			}
			rear++;
			queue[rear] = node;
		}

		static Node dequeue() {
			if (isEmpty()) {
				return null;
			}
			Node temp = queue[front];
			if (front == rear) {
				front = rear = 0;
			} else {
				front++;
			}
			return temp;
		}
	}
}
