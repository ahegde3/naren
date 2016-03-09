package com.narren.graph;

public class DijsktraAdjanceyMatrix {

	static boolean[] visited;
	static int[] dist;
	static int[][] graph;
	
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
	}
}
