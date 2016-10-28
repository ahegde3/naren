package com.narren.graph;

import java.util.Scanner;

public class DfsRevisited {

	/**
1
9 8
1 3
1 2
1 4
9 6
6 3
2 5
2 8
4 7
1
	 * @author naren
	 *
	 */
	static class Node {
		int id;
		Node next;
	}
	
	static Node addToList(Node node, int id) {
		Node temp = new Node();
		temp.id = id;
		temp.next = node;
		return temp;
	}
	
	static Node[] adjanceyList;
	static Node[] stack;
	static int front;
	static boolean[] visited;
	
	static class Stack {
		
		static boolean isEmpty() {
			return front == 0;
		}
		
		static boolean push(Node node) {
			if (node == null) {
				return false;
			}
			front++;
			stack[front] = node;
			return true;
		}
		
		static Node pop() {
			if (isEmpty()) {
				return null;
			}
			Node temp = stack[front];
			stack[front] = null;
			front--;
			return temp;
		}
		
		static Node peek() {
			if (isEmpty()) {
				return null;
			}
			Node temp = stack[front];
			return temp;
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		
		while (T > 0) {
			int N = sc.nextInt();
			int E = sc.nextInt();
			adjanceyList = new Node[N + 1];
			stack = new Node[2 * E];
			visited = new boolean[N + 1];
			front = 0;

			while (E > 0) {
				int n1 = sc.nextInt();
				int n2 = sc.nextInt();
				adjanceyList[n1] = addToList(adjanceyList[n1], n2);
				adjanceyList[n2] = addToList(adjanceyList[n2], n1);
				E--;
			}
			int s = sc.nextInt();
			process(s);
			T--;
		}
		
	}
	
	static int pushOperaation(Node temp) {
		int pushed = 0;
		while (temp != null) {
			if (!visited[temp.id]) {
				boolean ret = Stack.push(temp);
				visited[temp.id] = true;
				if (ret) {
					pushed++;
				}
			}
			temp = temp.next;
		}
		return pushed;
	}
	
	static void process(int start) {
		visited[start] = true;
		Node temp = adjanceyList[start];
		dfs(pushOperaation(temp));
	}
	
	static void dfs(int pushed) {
		if (pushed < 1) {
			Node temp = Stack.pop();
			System.out.println(temp.id);
			//return;
		}
		Node peekNode = Stack.peek();
		if (peekNode == null) {
			return;
		}
		Node look = adjanceyList[peekNode.id];
		dfs(pushOperaation(look));
	}
}
