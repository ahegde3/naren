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
		AdjanceyList temp = new AdjanceyList();
		temp.id = id;
		temp.weight = weight;
		temp.next = node;
		return temp;
	}
	
	static void swapEdges (EdgeQueue[] list, int i, int j) {
		EdgeQueue temp = list[i];
		list[i] = list[j];
		list[j] = temp;
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
		AdjanceyList startIndex = list[start];
		int totalCost = 0;
		while (startIndex != null) {
			visited[start] = true;
			while (startIndex != null) {
				if (!visited[startIndex.id]) {
					EdgeQueue edge = new EdgeQueue();
					edge.start = start;
					edge.end = startIndex.id;
					edge.weight = startIndex.weight;
					BinaryMinHeap.enqueue(binaryMinHeap, edge);
				}
				startIndex = startIndex.next;
			}
			EdgeQueue edge = BinaryMinHeap.getMinimum(binaryMinHeap);
			if (edge != null) {
				if (!visited[edge.end]) 
					totalCost += edge.weight;
				startIndex = list[edge.end];
				start = edge.end;
			}
		}
		System.out.println(totalCost);
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int E = sc.nextInt();
		AdjanceyList[] list = new AdjanceyList[N + 1];
		binaryMinHeap = new EdgeQueue[N + 1];
		while (E > 0) {
			int n1 = sc.nextInt();
			int n2 = sc.nextInt();
			int w = sc.nextInt();
			list[n1] = addToList(list[n1], n2, w); 
			list[n2] = addToList(list[n2], n1, w);
			//addToEdgeQ(edeges, n1, n2, w);
			E--;
		}
		int start = sc.nextInt();
		process(list, binaryMinHeap, start);

	}
	
	static class BinaryMinHeap {
	
		static void swap(EdgeQueue[] queue, int i, int j) {
			EdgeQueue temp = queue[i];
			queue[i] = queue[j];
			queue[j] = temp;
		}
		
		static void enqueue(EdgeQueue[] queue, EdgeQueue edge) {
			binaryHeapIndex++;
			queue[binaryHeapIndex] = edge;
			int parent = binaryHeapIndex / 2;
			int cur = binaryHeapIndex;
			
			while (parent > 0 && queue[parent].weight > edge.weight) {
				swap(queue, parent, cur) ;
				cur = parent;
				parent = cur / 2;
			}
		}
		
		static EdgeQueue getMinimum(EdgeQueue[] queue) {
			EdgeQueue edge = queue[1];
			dequeue(queue, 1);
			return edge;
		}
		
		static void dequeue(EdgeQueue[] queue, int index) {
			swap(queue, binaryHeapIndex, index);
			queue[binaryHeapIndex] = null;
			binaryHeapIndex--;
			int child = index * 2;
			int tempIndex = index;
			
			while (child <= binaryHeapIndex) {
				if (queue[tempIndex].weight > queue[child].weight ||
						(child + 1 <= binaryHeapIndex && queue[tempIndex].weight > queue[child + 1].weight)) {
					if (child + 1 <= binaryHeapIndex && queue[child].weight > queue[child + 1].weight) {
						swap(queue, child + 1, tempIndex);
						tempIndex = child + 1;
						child = child * 2 + 1;
						continue;
					}
					if (queue[tempIndex].weight > queue[child].weight) {
						swap(queue, tempIndex, child);
						tempIndex = child;
						child = child * 2;
						continue;
					}

				}
				break;
			}
		}
		
		static void heapify(EdgeQueue[] queue, int index) {
			int parent = index / 2;
			int child = index * 2;
			if (queue[index].weight < queue[parent].weight) {
				//go upward
				while (queue[parent].weight > queue[index].weight) {
					swap(queue, parent, index) ;
					index = parent;
					parent = index/2;
				}
			} else if (queue[index].weight > queue[child].weight ||
					queue[index].weight > queue[child + 1].weight) {
				// go downwards
				while (queue[child].weight < queue[index].weight ||
						queue[index].weight > queue[child + 1].weight) {
					if (queue[child].weight < queue[index].weight) {
						swap(queue, child, index) ;
						index = child;
						child = index * 2;
					} else if (queue[index].weight > queue[child + 1].weight) {
						swap(queue, child + 1, index) ;
						index = child + 1;
						child = index * 2;
					}

				}
			}
		}
		
		static void buildHeap(EdgeQueue[] queue) {
			for (int i = binaryHeapIndex / 2; i > 0; i--) {
				heapify(queue, i);
			}
		}
	}
}
