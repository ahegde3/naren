package com.narren.coding.practice;

import java.util.Scanner;

/**
 *
1
8 6
1 2
1 3
2 6
6 7
3 5
3 4
1
 * @author ns.bisht
 *
 */
public class BFSShortestReach {

	static Node[] queue;
	static Node[] adjancyList;
	static boolean[] visited;
	static int[] level;
	static int rear;
	static int front;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while (T > 0) {
			int N = sc.nextInt();
			int E = sc.nextInt();
			adjancyList = new Node[N + 1];
			level = new int[N + 1];
			visited = new boolean[N + 1];
			queue = new Node[2* (N + 1)];
			while (E > 0) {
				int n1 = sc.nextInt();
				int n2 = sc.nextInt();
				adjancyList[n1] = insertToList(adjancyList[n1], n2);
				adjancyList[n2] = insertToList(adjancyList[n2], n1);
				E--;
			}
			int s = sc.nextInt();
			process(s, 0);
			for (int i = 1; i < level.length; i++) {
				if (i != s) {
					System.out.print(level[i] > 0 ? (level[i] * 6) : (-1));
					System.out.print(" ");
				}

			}
			System.out.println();
			T--;
		}

	}

	static void process (int s, int curLevel) {
		visited[s] = true;
		level[s] = curLevel;
		while(true) {
			Node node = adjancyList[s];
			while (node != null) {
				if(!visited[node.value]) {
					Queue.enqueue(node);
					level[node.value] = level[s] + 1;	
				}
				node = node.next;
			}
			visited[s] = true;
			Node newNode = Queue.dequeue();
			if(newNode == null) {
				break;
			}
			s = newNode.value;
		}


	}

	static class Node {
		int value;
		Node next;
	}

	static Node insertToList(Node node, int value) {
		Node temp = new Node();
		temp.value = value;
		temp.next = node;
		return temp;
	}

	static class Queue {

		static void enqueue(Node node) {
			if (front == 0) {
				++front;
			}
			queue[++rear] = node;
		}

		static Node peek() {
			Node node = queue[front];
			return node;
		}

		static Node dequeue() {
			Node node = queue[front];
			if (front == rear) {
				front = rear = 0;
			} else {
				front++;	
			}

			return node;
		}
	}
}
