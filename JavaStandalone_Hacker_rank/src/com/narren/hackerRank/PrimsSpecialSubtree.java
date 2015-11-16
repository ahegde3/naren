package com.narren.hackerRank;

import java.util.Scanner;

public class PrimsSpecialSubtree {

	static int edgeQIndex = -1;
	static boolean[] visited = new boolean[3000];
	static EdgeQueue[] binaryMinHeap;
	static int binaryHeapIndex;
	
	static class EdgeQueue {
		int start;
		int end;
		int weight;
	}
	
	static class AdjanceyList {
		int id;
		int weight;
		AdjanceyList next;
	}
	
	static AdjanceyList addToList (AdjanceyList node, int id, int weight) {
		AdjanceyList tempNode = node;
		AdjanceyList temp = new AdjanceyList();
		temp.id = id;
		temp.weight = weight;
		if (tempNode != null) {
			AdjanceyList p = node;
			AdjanceyList q = p;
			while (q != null && q.weight < weight) {
				p = q;
				q = q.next;
			}
			if (q == p) {
				temp.next = p;
				return temp;
			}
			AdjanceyList tempNode1 = p.next;
			p.next = temp;
			temp.next = tempNode1;
			return p;
		}
		if (node == null) {
			temp.next = node;
			return temp;
		} else {
			node.next = temp;
			return node;
		}
	}
	
	static void swapEdges (EdgeQueue[] list, int i, int j) {
		EdgeQueue temp = list[i];
		list[i] = list[j];
		list[j] = temp;
	}
	
	static void addToEdgeQ(EdgeQueue[] edeges, int s, int e, int w) {
		EdgeQueue edge = new EdgeQueue();
		edge.start = s;
		edge.end = e;
		edge.weight = w;
		int tempEdgeIndex = edgeQIndex;
		if (edgeQIndex < 0) {
			edeges[edgeQIndex + 1] = edge;
			edgeQIndex++;
		} else {
			if (edeges[edgeQIndex].weight > w) {
				while (tempEdgeIndex >= 0 && edeges[tempEdgeIndex].weight > w) {
					edeges[tempEdgeIndex + 1] = edge;
					swapEdges(edeges, tempEdgeIndex, tempEdgeIndex + 1); 
					tempEdgeIndex--;
				}
				edgeQIndex ++;
			} else {
				edeges[edgeQIndex + 1] = edge;
				edgeQIndex++;
			}
		}
	}
	
	static EdgeQueue getLeast (EdgeQueue[] edeges, int start) {
		int len = 0;
		while (len < edeges.length) {
			if(edeges[len].start == start && !visited[edeges[len].end]) {
				return edeges[len];
			} else if (edeges[len].end == start && !visited[edeges[len].start]) {
				return edeges[len];
			}
			len++;
		}
		return null;
	}
	
	static void process (AdjanceyList[] list, EdgeQueue[] edeges, int start) {
		EdgeQueue edge = getLeast(edeges, start);
		visited[start] = true;
		int totalWeight = 0;
		while (edge != null) {
			EdgeQueue edge1 = getLeast(edeges, edge.start);
			EdgeQueue edge2 = getLeast(edeges, edge.end);
			if (edge1.weight <= edge2.weight) {
				visited[edge.start] = true;
			} else {
				visited[edge.end] = true;
			}
			int next = edge.start == start ? edge.end : start;
			totalWeight += edge.weight;
		}
		
		
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int E = sc.nextInt();
		AdjanceyList[] list = new AdjanceyList[N + 1];
		EdgeQueue[] edeges = new EdgeQueue[N + 1];
		while (E > 0) {
			int n1 = sc.nextInt();
			int n2 = sc.nextInt();
			int w = sc.nextInt();
			list[n1] = addToList(list[n1], n2, w); 
			list[n2] = addToList(list[n2], n1, w);
			addToEdgeQ(edeges, n1, n2, w);
			E--;
		}
		int start = sc.nextInt();
		process(list, edeges, start);

	}
	
	static class BinaryMinHeap {
		
		static void enqueue(EdgeQueue[] queue, EdgeQueue edge) {
			queue[binaryHeapIndex] = edge;
			
			while () {
				
			}
		}
	}
}
