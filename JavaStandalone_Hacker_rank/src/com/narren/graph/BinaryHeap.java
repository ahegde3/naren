package com.narren.graph;

class PriorityElement {
	int weight;
	
}
public class BinaryHeap {

	PriorityElement[] pQueue = new PriorityElement[10];
	int qHead = 1;
	public static void main(String[] args) {
		
	}
	
	/**
	 * Insert into the priority queue
	 * @param element
	 */
	void insert (PriorityElement element) {
		pQueue[qHead] = element;
		int tempHead = qHead;
		qHead++;
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
	
	
}
