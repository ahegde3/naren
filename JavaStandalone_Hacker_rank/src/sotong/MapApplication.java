package sotong;

import java.util.Scanner;

/**
 * 
INPUT

2
5 4
1 0 0 0 0
0 0 0 0 0
0 2 0 4 0
9 0 0 9 0
0 0 0 3 0
3 3
1 0 0
0 2 9
0 9 3 

OUTPUT
18
0
 * @author nsbisht
 *
 */
public class MapApplication {

	static int min = Integer.MAX_VALUE;
	static int N;
	static int M;
	static int[] shortestPathArr = new int[1000];
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while(T > 0) {
			N = sc.nextInt();
			M = sc.nextInt();
			int[][] input = new int[N][N];
			for(int i = 0; i < N; i ++) {
				for(int j = 0; j < N; j++) {
					input[i][j] = sc.nextInt();
				}
			}
			System.out.println(process(input));
			T--;
		}
	}
	
	static int process(int[][] input) {
		int res = 1;
		for(int k = 1; k < M; k++) {
			for(int i = 0; i < N; i++) {
				boolean foundPath = false;
				for(int j = 0; j < N; j++) {
					if(input[i][j] == k) {
						foundPath = true;
						findMinimumPath(input, k, k + 1, 0, i, j, new int[N][N]);
						break;
					}
				}
				if(foundPath) {
					break;
				}
			}
			if(min != Integer.MAX_VALUE) {
				for(int i = 0; i < 1000; i++) {
					if(shortestPathArr[i] > 0) {
						res = res * shortestPathArr[i];
						break;
					}
				}
			} else {
				res = 0;
			}
			min = Integer.MAX_VALUE;
			shortestPathArr = new int[1000];
		}
		return res;
	}
	
	static int findMinimumPath(int[][] input, int s, int d, int steps, int curi, int curj, int[][]visited) {
		if(min < steps) {
			return Integer.MAX_VALUE;
		}
		if(curi < 0 || curj < 0 || curi >= N || curj >= N) {
			return Integer.MAX_VALUE;
		}
		if(input[curi][curj] == 9) {
			return Integer.MAX_VALUE;
		}
		
		if(input[curi][curj] == d) {
			shortestPathArr[steps] = shortestPathArr[steps] + 1;
			return steps;
		}
		if(visited[curi][curj] == 1) {
			return Integer.MAX_VALUE;
		}
		visited[curi][curj] = 1;
		
		// Up
		int s1 = findMinimumPath(input, s, d, steps + 1, curi - 1, curj, getVisited(visited));

		// Down
		int s2 = findMinimumPath(input, s, d, steps + 1, curi + 1, curj, getVisited(visited));

		// Right
		int s3 = findMinimumPath(input, s, d, steps + 1, curi, curj + 1, getVisited(visited));

		// Left
		int s4 = findMinimumPath(input, s, d, steps + 1, curi, curj - 1, getVisited(visited));
		
		min = Math.min(min, Math.min(Math.min(s1, s2), Math.min(s3, s4)));
		return min;
	}
	
	static int[][] getVisited(int[][] vis) {
		int visited[][] = new int[N][N];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				visited[i][j] = vis[i][j];
			}
		}
		return visited;
	}
}
