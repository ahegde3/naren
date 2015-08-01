package com.narren.hackerRank;

import java.util.Scanner;

import sun.misc.Queue;

class Node {
	int value;
	Node next;
	boolean visited;
}
public class BreadthFirstSearchShortestReach {
	/**
	 * Head insertion
	 * @param head
	 * @param value
	 * @return
	 */
	private static Node insert (Node head, int value) {
		Node p = new Node();
		p.value = value;
		p.next = head;
		return p;
	}
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		int T = s.nextInt();
		while (T > 0) {
			Node[] adjancenyList;
			int[] level;
			int V = s.nextInt();
			int E = s.nextInt();
			adjancenyList = new Node[V + 1];
			level = new int[V + 1];
			while (E > 0) {
				int x = s.nextInt();
				int y = s.nextInt();
				adjancenyList[x] = insert(adjancenyList[x], y);
				adjancenyList[y] = insert(adjancenyList[y], x);
				E--;
			}
			int start = s.nextInt();
			System.out.println("\nAdjacency List -\n");
			for (int i = 1; i <= V; ++i) {
				System.out.println(i + "-> ");

				Node temp = adjancenyList[i];

				while (temp != null) {
					System.out.println(temp.value + " -> ");
					temp = temp.next;
				}

				System.out.println("NULL\n");
			}
			bfs(adjancenyList, level, start);
			for (int i = 1; i < level.length; i++) {
				if (start != i) {
					if (level[i] == 0) {
						System.out.print("-1 ");
					} else {
						int dis = 6 * level[i];
						System.out.print(dis + " ");
					}
				}
			}
			T--;
		}
	}
	static void bfs(Node[] list, int[] lev, int s) {
		Queue queue = new Queue();
		Node root = list[s];
		queue.enqueue(s);
		int level = 0;
		boolean[] visited = new boolean[list.length];
		lev[s] = level;
		visited[s] = true;
		while (!queue.isEmpty()) {
			try {
				int pos = (int) queue.dequeue();
				Node r = list[pos];
				if(lev[pos] < 1/* && pos != s*/) {
					level++;
				}
				while (r != null) {
					pos = r.value;
					if(!visited[pos]) {
						queue.enqueue(pos);
						visited[pos] = true;
						if (pos != s) {
							lev[pos] = level;
						}
					}
					r = r.next;

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * visit all the node from this node
	 * @param node
	 */
	static void visit (Node node, int[] lev, Node[] list) {
		
	}

}
