package com.narren.hackerRank;

import java.util.Scanner;

public class DijkstraShortestReach2 {

	/**
1
5 6
1 2 24
1 4 20
1 3 3
4 3 12
4 5 3
3 5 10
1
	 */
	static int root;
	static boolean visited[];
	static int[] distance;
	
	static class NodeItem {
		int weight;
		NodeItem next;
		int id;
	}

	private static NodeItem addToList(NodeItem node,  int weight, int id) {
		NodeItem nodeItem = new NodeItem();
		nodeItem.weight = weight;
		nodeItem.next = node;
		nodeItem.id = id;
		return nodeItem;
	}
	

	static void process (NodeItem[] list, int start) {
		NodeItem node = list[start];
		while (node != null) {
			if (node.id != start) {
				if (distance[node.id] != 0) {
					if (distance[node.id] > node.weight) {
						distance[node.id] = node.weight;	
					}
				} else {
					distance[node.id] = node.weight;
				}
			}
			node = node.next;
		}
		int i = 1;
		while (i < list.length) {
			if (i != start) {
				 node = list[i];
				while (node != null) {
					if (node.id != start) {
						if (distance[node.id] != 0) {
							if (distance[node.id] > (node.weight + distance[i])) {
								distance[node.id] = node.weight + distance[i];	
							}
						} else {
							distance[node.id] = node.weight + distance[i];
						}
					}
					node = node.next;
				}
		
			}
			i++;
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while (T > 0) {
			int N = sc.nextInt();
			int E = sc.nextInt();
			NodeItem[] adjanceyList = new NodeItem[N + 1];
			int[][] graph = new int[N][N];
			visited = new boolean[N];
			distance = new int[N + 1];
			while (E > 0) {
				int n1 = sc.nextInt();
				int n2 = sc.nextInt();
				int weight = sc.nextInt();
				adjanceyList[n1] = addToList(adjanceyList[n1], weight, n2);
				adjanceyList[n2] = addToList(adjanceyList[n2], weight, n1);
				E--;
			}
			root = sc.nextInt();
			process(adjanceyList, root);
			for (int i = 1; i < distance.length; i++) {
				if (i != root) {
					if (distance[i] == 0) {
						System.out.print("-1");
					} else {
						System.out.print(distance[i]);
					}
					if (i < distance.length - 1) {
						System.out.print(" ");
					}
				}
			}
			System.out.println();
			T--;
		}
	}

}
