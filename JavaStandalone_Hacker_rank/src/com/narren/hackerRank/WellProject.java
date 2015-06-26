package com.narren.hackerRank;

import java.util.Scanner;

public class WellProject {

	public static void main(String[] args) {
		/**
2
3
0 1 4
1 0 2
4 2 0
4
0 4 9 21
4 0 8 17
9 8 0 16
21 17 16 0

		 */
		Scanner in = new Scanner(System.in);
		int T = in.nextInt();
		while (T > 0) {
			int L = in.nextInt();
			WellProject.PGraph graph = new WellProject().new PGraph(L);

			int i,j,a,b,w;
			for ( i = 0; i < L; i++)
				for( j = 0; j < L; j++) {
					if (i == j) {
						in.nextInt();
						continue;
					}
					int weight = in.nextInt();
					graph.weight[i][j] = weight;
				}

			for (i = 0; i< graph.v; i++) {
				graph.p[i]=graph.visited[i]=0;
				graph.d[i]=Integer.MAX_VALUE;
			}
			graph.algo();
			T--;
		}
	}

	class PGraph {
		int weight[][];
		int visited[];
		int d[];
		int p[];
		int v,e;

		public PGraph(int nodes) {
			weight = new int[nodes][nodes];
			visited = new int [nodes];
			d = new int[nodes];
			p = new int[nodes];
			v = nodes;
			e = nodes;
		}
		

		void algo ()
		{
			int current,total,mincost,i;
			current=1;d[current]=0;
			total=1;
			visited[current]=1;
			while(total!=v)
			{
				for (i = 0; i < v; i++)
				{  
					if(weight[current][i]!=0)
						if(visited[i]==0)
							if(d[i]>weight[current][i])
							{
								d[i]=weight[current][i];
								p[i]=current;
							}
				}
				mincost=Integer.MAX_VALUE;
				for (i = 0; i < v; i++)
				{
					if(visited[i]==0)
						if(d[i]<mincost)
						{
							mincost=d[i];
							current=i;
						}
				}
				visited[current]=1;
				total++;
			}
			mincost=0;
			for(i = 0; i < v; i++)
				mincost=mincost+d[i];
			System.out.print(mincost);
		}
	}
}
