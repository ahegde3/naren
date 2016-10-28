package com.narren.graph;

import java.util.Scanner;

class PriorityElement {
	int weight;
	
}
/**
 * 
 * @author ns.bisht
 *
 */
public class BinaryHeap {

	static PriorityElement[] pQueue = null;
	static int qHead = 0;
	public static void main(String[] args) {
		/**
		 13
10
8
7
5
6
15
25
20
11
9
17
17
9
		 */
		Scanner s = new Scanner(System.in);
		int count = s.nextInt();
		pQueue = new PriorityElement[count + 1];
		for (int i = 1; i <= count; i++) {
			PriorityElement e = new PriorityElement();
			e.weight = s.nextInt();
			//insert(e);
			pQueue[i] = e;
			qHead++;
		}
		heapify();
		for (int i = 1; i <= count; i++) {
			System.out.println(pQueue[i].weight);
		}
	}
	
	/**
	 * Insert into the priority queue
	 * @param element
	 */
	static void insert (PriorityElement element) {
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
	static void swap(int i, int j) {
		PriorityElement temp = pQueue[i];
		pQueue[i] = pQueue[j];
		pQueue[j] = temp;
	}
	
	static void heapify () {
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
