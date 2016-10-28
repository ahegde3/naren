package com.narren.graph;

public class DijkstraAdjanceyMatrix {
	static int[][] graph;
	static boolean[] visited;
	static int[] dest;
	static int N;

	static int getMinIndex() {
		int minIndex = -1;
		int min = Integer.MAX_VALUE;
		for(int i = 0; i < N; i++) {
			if(!visited[i] && dest[i] < min) {
				min = dest[i];
				minIndex = i;
			}
		}
		return minIndex;
	}

	static void dijkstra(int src) {

		for(int i = 0; i < N; i++) {
			dest[i] = Integer.MAX_VALUE;
		}

		dest[src] = 0;
		for(int i = 0; i < N; i++ ) {

			int u = getMinIndex();
			visited[u] = true;
			for (int v = 0; v < N; v++) {

				if(!visited[v] && graph[u][v] != 0
						&& dest[u] != Integer.MAX_VALUE
						&& dest[u] + graph[u][v] < dest[v]) {
					dest[v] = dest[u] + graph[u][v];
				}
			}
		}
	}

	public static void main(String[] args) {
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
		
		 N = 9;
		 dest = new int[N];
		 visited = new boolean[N];
	}
}
