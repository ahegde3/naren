package com.narren.hackerRank;

import java.util.Scanner;


public class BFSShortestReach {

	/*
1
9 8
1 2
2 3
1 4
4 5
5 6
5 7
4 8
8 9
3
	 */
	static int root;
	
	private static void process(int[][] graph, int s) {
		connect(graph, s, 1);
		for (int i = s; i < graph.length; i ++) {
			for (int j  = 0; j < graph[i].length; j++) {
				if (j != i) {
					if (graph[i][j] == 0) {
						System.out.print("-1 ");
					} else {
						System.out.print(graph[i][j] * 6 + " ");
					}
				}
			}
			break;
		}
	}
	
	private static void connect (int[][] graph, int s, int level) {
		for (int i = s; i < graph.length; i ++) {
			for (int j  = 0; j < graph[i].length; j++) {
				if (graph[i][j] == 1) {
					
					if ((graph[root][j] == 0 ||
							(graph[root][j] > 0 && level < graph[root][j]) || level == 1) && j != root) {
						graph[root][j] = level;		
						connect(graph, j, level + 1);
					}
				}
			}
			break;
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while (T > 0) {
			int N = sc.nextInt();
			int E = sc.nextInt();
			int[][] graph = new int[N][N];
			while (E > 0) {
				int n1 = sc.nextInt();
				int n2 = sc.nextInt();
				graph[n1-1][n2-1] = 1;
				graph[n2-1][n1-1] = 1;
				E--;
			}
			root = sc.nextInt();
			root -= 1;
			process(graph, root);
			System.out.println();
			T--;
		}
	}
}
