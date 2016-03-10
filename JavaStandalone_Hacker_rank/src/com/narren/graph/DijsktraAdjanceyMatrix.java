package com.narren.graph;

public class DijsktraAdjanceyMatrix {

	static boolean[] visited;
	static int[] dist;
	static int[][] graph;
	static int N; //No. of nodes

	/**
	 * This gets the index of the vertex having minimum weight  
	 * @return
	 */
	static int getMinimumIndex() {
		int min = Integer.MAX_VALUE;
		int minIndex = -1;
		for(int i = 0; i < graph.length; i++) {
			if(!visited[i] && dist[i] <= min) {
				min = dist[i];
				minIndex = i;
			}
		}
		return minIndex;
	}

	static void dijkstra(int srcIndex) {
		for(int i = 0; i < dist.length; i++) {
			dist[i] = Integer.MAX_VALUE;
		}
		// the source
		dist[srcIndex] = 0;

		for (int i = 0; i < N - 1; i++) {
			int u = getMinimumIndex();

			// Mark the picked vertex as processed
			visited[u] = true;

			// Update dist value of the adjacent vertices of the picked vertex.
			for (int v = 0; v < N; v++)

				// Update dist[v] only if is not in sptSet, there is an edge from 
				// u to v, and total weight of path from src to  v through u is 
				// smaller than current value of dist[v]
				if (!visited[v] && graph[u][v] != 0
				&& dist[u] != Integer.MAX_VALUE 
				&& dist[u]+graph[u][v] < dist[v]) {
					dist[v] = dist[u] + graph[u][v];
				}
		}
	}

	public static void main(String[] args) {
		N = 9;
		graph = new int[][]{{0, 4, 0, 0, 0, 0, 0, 8, 0},
				{4, 0, 8, 0, 0, 0, 0, 11, 0},
				{0, 8, 0, 7, 0, 4, 0, 0, 2},
				{0, 0, 7, 0, 9, 14, 0, 0, 0},
				{0, 0, 0, 9, 0, 10, 0, 0, 0},
				{0, 0, 4, 0, 10, 0, 2, 0, 0},
				{0, 0, 0, 14, 0, 2, 0, 1, 6},
				{8, 11, 0, 0, 0, 0, 1, 0, 7},
				{0, 0, 2, 0, 0, 0, 6, 7, 0}
		};
		dist = new int[N];
		visited = new boolean[N];
		dijkstra(0);
	}
}
