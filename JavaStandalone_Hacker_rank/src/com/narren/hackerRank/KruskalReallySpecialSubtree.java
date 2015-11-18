package com.narren.hackerRank;

import java.util.Scanner;

public class KruskalReallySpecialSubtree {

	static Edge[] edgeList;
	static int listPointerIndex = 0;
	static boolean visited[] = new boolean[3000];;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int M = sc.nextInt();
		edgeList = new Edge[M + 1];
		while (M > 0) {
			int n1 = sc.nextInt();
			int n2 = sc.nextInt();
			int w = sc.nextInt();
			Edge edge = new Edge();
			edge.start = n1;
			edge.end = n2;
			edge.weight = w;
			BinaryMinHeap.enqueue(edge);
			M--;
		}
		int s = sc.nextInt();
		process();
	}
	
	static boolean isCycle(Edge edge) {
		return visited[edge.start] && visited[edge.end];
	}
	
	static void process () {
		int totalCost = 0;
		while (listPointerIndex > 0) {
			Edge edge = BinaryMinHeap.getMinimum();
			if (!isCycle(edge)) {
				visited[edge.start] = true;
				visited[edge.end] = true;
				totalCost += edge.weight;
			}
		}
		System.out.println(totalCost);
	}
	
	static void swap (int i, int j) {
		Edge temp = edgeList[i];
		edgeList[i] = edgeList[j];
		edgeList[j] = temp;
	}

	static int getEffectiveWeight(Edge edge) {
		return edge.end + edge.start + edge.weight;
	}

	static class Edge {
		int start;
		int end;
		int weight;
	}

	static class BinaryMinHeap {
		
		static Edge getMinimum () {
			Edge temp = edgeList[1];
			dequeue(1);
			return temp;
		}

		static void enqueue(Edge edge) {
			edgeList[listPointerIndex + 1] = edge;
			listPointerIndex++;
			int curIndex = listPointerIndex;
			int parent = listPointerIndex / 2;

			while (parent > 0) {
				if (edgeList[parent].weight > edge.weight) {
					swap(parent, curIndex);
					curIndex = parent;
					parent = parent / 2;
					continue;
				} else if (edgeList[parent].weight == edge.weight) {
					if (getEffectiveWeight(edgeList[parent]) > getEffectiveWeight(edge)) {
						swap(parent, curIndex);
						curIndex = parent;
						parent = parent / 2;
						continue;
					}

				}
				break;
			}

		}	

		static void dequeue(int index) {
			swap(index, listPointerIndex);
			edgeList[listPointerIndex] = null;
			listPointerIndex--;
			int child = index * 2;
			int tempIndex = index;

			while (child <= listPointerIndex) {
				if (edgeList[tempIndex].weight > edgeList[child].weight ||
						(child + 1 <= listPointerIndex && edgeList[tempIndex].weight > edgeList[child + 1].weight)) {
					if (child + 1 <= listPointerIndex && edgeList[child].weight > edgeList[child + 1].weight) {
						swap(child + 1, tempIndex);
						tempIndex = child + 1;
						child = child * 2 + 1;
						continue;
					}
					if (edgeList[tempIndex].weight > edgeList[child].weight) {
						swap(tempIndex, child);
						tempIndex = child;
						child = child * 2;
						continue;
					}

				}
				break;
			}

		}
	}
}
