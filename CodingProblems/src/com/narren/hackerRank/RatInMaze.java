package com.narren.hackerRank;

public class RatInMaze {

	static int N = 5;
	static boolean[][] visited = new boolean[N][N];
	static int[][] arr = { { 1, 0, 1, 1,1 }, { 1, 1, 1, 0,1 }, { 0, 0,0, 1, 1 },
		{ 0, 0, 0, 1,0 },{ 0, 0,0, 1, 1 } };

	public static void main(String[] args) {
		process(0, 0);
	}

	static void process(int i, int j) {
		if (i >= N || j >= N  || i < 0 || j < 0) {
			return;
		}

		if(arr[i][j] == 0) {
			return;
		}

		if(visited[i][j]) {
			return;
		}

		System.out.println(i + " " + j);

		//Go down
		visited[i][j] = true;
		process(i + 1, j);

		//Go right
		visited[i][j] = true;
		process(i, j + 1);

		//Go up
		visited[i][j] = true;
		process(i - 1, j);

		//Go left
		visited[i][j] = true;
		process(i, j - 1);
	}
}
