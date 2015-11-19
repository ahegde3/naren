package com.narren.graph;

import java.util.Scanner;

public class BfsRevisited {

	static class Node {
		int id;
		Node next;
	}

	static Node[] queue;

	static int curInsertIndex = 0;
	static int curHeadIndex = 0;
	static boolean[] visited;
	static int level[];
	static int curLevel;

	static Node addToAdjanceyList(Node node, int id) {
		Node temp = new Node();
		temp.id = id;
		temp.next = node;
		return temp;
	}

	static Node[] adjanceyList;

	static class Queue {		
		static void enqueue(Node node) {
			if (curHeadIndex == 0) {
				curHeadIndex++;
			}
			queue[curInsertIndex + 1] = node;
			curInsertIndex++;
		}

		static Node dequeue() {
			Node temp = queue[curHeadIndex];
			if (queue[curHeadIndex + 1] != null) {
				curHeadIndex++;	
			} else {
				curHeadIndex = 0;
			}

			return temp;
		}
		
		static boolean isQueueEmpty() {
			return curHeadIndex == 0;
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		
		while (T > 0) {
			int N = sc.nextInt();
			int E = sc.nextInt();
			adjanceyList = new Node[N + 1];
			queue = new Node[N + 1];
			visited = new boolean[N + 1];
			level = new int[N + 1];

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
	
	static void enqueue (Node node) {
		while (node != null) {
			if (!visited[node.id]) {
				Queue.enqueue(node);
				level[node.id] = curLevel + 1;
			}
			node = node.next;
		}
		curLevel++;
	}
	
	static void process(int start) {

		level[start] = 0;
		Node node = adjanceyList[start];
		visited[start] = true;
		enqueue(node);
		while(!Queue.isQueueEmpty()) {
			Node temp = Queue.dequeue();
			if (temp != null) {
				visited[temp.id] = true;
				enqueue(adjanceyList[temp.id]);				
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
