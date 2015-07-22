package com.narren.graph;

import java.util.Scanner;

public class DijsktraAlgo {

	public static void main(String[] args) {
		int vertices, edges, i, v, v1, v2;
		Scanner s = new Scanner(System.in);

		vertices = s.nextInt();
		edges = s.nextInt();
		/* This is the table of our Adjacency List
		 * Each element holds a Linked List
		 */
		Node[] adjanceyList = new Node[vertices + 1];

		for (i = 0; i <= vertices; ++i) {
			//Initialising our arrays
			adjanceyList[i] = null;
		}

		for (i = 1; i <= edges; ++i) {
			v1 = s.nextInt();
			v2 = s.nextInt();
			adjanceyList[v1] = insert(adjanceyList[v1], v2);
		}

		//Printing Adjacency List
		System.out.println("\nAdjacency List -\n");
		for (i = 1; i <= vertices; ++i) {
			System.out.println(i + "-> ");

			Node temp = adjanceyList[i];

			while (temp != null) {
				System.out.println(temp.value + " -> ");
				temp = temp.next;
			}

			System.out.println("NULL\n");
		}

	}
	static void dijsktra (Node[] adjanceyList) {
		int[] shortestArray = new int[adjanceyList.length];
		BinaryHeapHelper binaryHeap = new DijsktraAlgo().new BinaryHeapHelper();
		binaryHeap.pQueue = new PriorityElement[adjanceyList.length];
		for (int i = 0; i < adjanceyList.length; i++) {
			shortestArray[i] = Integer.MAX_VALUE;
		}
		
		shortestArray[1] = 0;
		PriorityElement pe = new DijsktraAlgo().new PriorityElement();
		pe.weight = 0;
		pe.index = 1;
		binaryHeap.insert(pe);
		
		while (!binaryHeap.isQueueEmpty()) {
			PriorityElement min = binaryHeap.extractMin();
			Node temp = adjanceyList[min.index];
			if (temp != null) {

				PriorityElement elem = new DijsktraAlgo().new PriorityElement();
				elem.weight = temp.value;
				elem.index = min.index;
				binaryHeap.insert(elem);
				while (temp.next != null) {
					PriorityElement ele = new DijsktraAlgo().new PriorityElement();
					ele.weight = temp.next.value;
					ele.index = min.index;
					binaryHeap.insert(ele);
					temp = temp.next;
				}				
			}

		}

	}

	class Node {
		int value;
		Node next;
		boolean visited;
	}
	/**
	 * This is exactly as Head Insertion of a Linked List.
   We choose Head Insertion to get an O(1) Insertion
   into an Adjacency List, Tail Insertion can also be used
	 * @param head
	 * @param value
	 * @return
	 */
	private static Node insert (Node head, int value) {
		Node p = new DijsktraAlgo().new Node();
		p.value = value;
		p.next = head;
		p.visited = false;
		return p;
	}
	class PriorityElement {
		int weight;
		int index;
	}

	class BinaryHeapHelper {

		PriorityElement[] pQueue = null;
		int qHead = 0;

		boolean isQueueEmpty() {
			return qHead == 0;
		}
		/**
		 * Insert into the priority queue
		 * @param element
		 */
		void insert (PriorityElement element) {
			qHead++;
			pQueue[qHead] = element;
			int tempHead = qHead;
			while (tempHead/2 > 0 && pQueue[tempHead/2].weight > element.weight) {
				swap(tempHead, tempHead/2);
				tempHead = tempHead/2;
			}
		}

		void delete (int index) {
			if (index != qHead) {
				swap(index, qHead);
				pQueue[qHead] = null;
				qHead--;
				if (pQueue[index].weight > pQueue[index/2].weight) {
					// percolating down
					int temp = index;
					while (pQueue[temp*2] != null && pQueue[temp*2].weight < pQueue[temp].weight) {
						swap(temp, temp*2);
						temp = temp*2;
					}

				} else if (pQueue[index].weight < pQueue[index/2].weight) {
					//percolating up
					int temp = index;
					while (temp/2 > 0 && pQueue[temp/2].weight > pQueue[temp].weight) {
						swap(temp, temp/2);
						temp = temp/2;
					}
				}
			} else {
				pQueue[qHead] = null;
				qHead--;
			}
		}
		void swap(int i, int j) {
			PriorityElement temp = pQueue[i];
			pQueue[i] = pQueue[j];
			pQueue[j] = temp;
		}

		PriorityElement extractMin() {
			PriorityElement pe = pQueue[1];
			delete(1);
			heapify();
			return pe;
		}

		void decreasePriority (int index, int newWeight) {
			PriorityElement pe = pQueue[index];
			pe.weight = newWeight;
			pQueue[index] = pe;
			heapify();
		}

		void heapify () {
			//Get the first parent from last.
			int parentIndex = qHead/2;

			while (parentIndex > 0) {
				int tempParentIndex = parentIndex;
				while (tempParentIndex <= qHead/2 &&
						(pQueue[tempParentIndex].weight > pQueue[tempParentIndex * 2].weight ||
								pQueue[tempParentIndex].weight > pQueue[tempParentIndex * 2 + 1].weight)) {
					if (pQueue[tempParentIndex * 2].weight > pQueue[tempParentIndex * 2 + 1].weight) {
						swap(tempParentIndex * 2 + 1, tempParentIndex); //swap parent and right child
						tempParentIndex = tempParentIndex * 2 + 1;
					} else {
						swap(tempParentIndex*2, tempParentIndex); //swap parent and left child
						tempParentIndex = tempParentIndex * 2;
					}
				}
				parentIndex--;
			}
		}
	}

}
