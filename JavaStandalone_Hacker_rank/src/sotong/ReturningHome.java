package sotong;

import java.util.Scanner;

public class ReturningHome {

	static int[][] graph;
	static int[] dest;
	static boolean[] visited;
	static int N;
	static int E;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while(T > 0) {
			E = sc.nextInt();
			N = sc.nextInt();
			graph = new int[N][N];
			dest = new int[N];
			visited = new boolean[N];
			while(E > 0) {
				int s = sc.nextInt();
				int e = sc.nextInt();
				int d = sc.nextInt();
				if(graph[s - 1][e - 1] > 0) {
					if(graph[s - 1][e - 1] > d) {
						graph[s - 1][e - 1] = d;
						graph[e - 1][s - 1] = d;	
					}

				} else {
					graph[s - 1][e - 1] = d;
					graph[e - 1][s - 1] = d;
				}
				E--;
			}
			dijkstra(N - 1);
			System.out.println(dest[0]);
			T--;
		}
	}
	
	static int getMinIndex() {
		int minINdex = -1;
		int min = Integer.MAX_VALUE;
		for(int i = 0; i < N; i++) {
			if(!visited[i] && dest[i] < min) {
				min = dest[i];
				minINdex = i;
			}
		}
		return minINdex;
	}
	
	static void dijkstra(int src) {
		for(int i = 0; i < N; i++) {
			dest[i] = Integer.MAX_VALUE;
		}
		
		dest[src] = 0;
		for(int i = 0; i < N; i++) {
			int u = getMinIndex();
			if(u < 0) {
				return;
			}
			visited[u] = true;
			for(int v = 0; v < N; v++) {
				if(!visited[v] && graph[u][v] != 0
						&& dest[u] != Integer.MAX_VALUE
						&& dest[u] + graph[u][v] < dest[v]) {
					dest[v] = dest[u] + graph[u][v];
				}
			}
		}
	}
}
