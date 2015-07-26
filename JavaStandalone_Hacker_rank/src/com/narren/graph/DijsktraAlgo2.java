package com.narren.graph;

import java.util.Scanner;

/* ==========  ========== ========== ========= */
//Dijkstra's Algorithm in C          //
//using Binary Min-Heap            //
//O(|E|log|V| + |V|log|V|) implementation   //
//                                 //
//Functions follow Pascal Case        //
//Convention and Variables          //
//follow Camel Case Convention        //
//The Data Structure is 1-indexed      //
/* ========== ========== ========== ========== */

class Node {
	int vertex, weight;
	Node next;
}

class Path {
	int distance, parent;
}

class Vertex {
	int label, value;
}

public class DijsktraAlgo2 {
	//Follows Head Insertion to give O(1) insertion
	Node AddEdge(Node head, int vertex, int weight) {
		Node p = new Node();;

		p.vertex = vertex;
		p.weight = weight;
		p.next = head;

		return p;
	}

	//Applies the heapify procedure - O(log N)
	void Heapify(Vertex minHeap[], int size, int i, int hashTable[])
	{
		Vertex temp;

		while ((2 * i) <= size) {
			if ((2 * i) + 1 > size) {
				if (minHeap[i].value > minHeap[2 * i].value) {
					hashTable[minHeap[i].label] = 2 * i;
					hashTable[minHeap[2 * i].label] = i;

					temp = minHeap[i];
					minHeap[i] = minHeap[2 * i];
					minHeap[2 * i] = temp;
				}

				break;
			}

			if (minHeap[i].value > minHeap[2 * i].value || minHeap[i].value > minHeap[2 * i + 1].value) { 
				if (minHeap[2 * i].value <= minHeap[(2 * i) + 1].value) {
					hashTable[minHeap[i].label] = 2 * i;
					hashTable[minHeap[2 * i].label] = i;

					temp = minHeap[2 * i];
					minHeap[2 * i] = minHeap[i];
					minHeap[i] = temp;

					i = 2 * i;
				} else if (minHeap[2 * i].value > minHeap[(2 * i) + 1].value) {
					hashTable[minHeap[i].label] = 2 * i + 1;
					hashTable[minHeap[2 * i + 1].label] = i;

					temp = minHeap[(2 * i) + 1];
					minHeap[(2 * i) + 1] = minHeap[i];
					minHeap[i] = temp;

					i = (2 * i) + 1;
				}
			} else {
				break;
			}
		}
	}

	//Build Heap Procedure - O(N)
	void BuildHeap(Vertex minHeap[], int size, int hashTable[])
	{
		int i;

		for (i = size / 2; i >= 1; --i) {
			Heapify(minHeap, size, i, hashTable);
		}
	}

	//Searches for a Node in the Heap in O(1) time and Decreases its value
	//Then calls Heapify() on it's parent to adjust heap -> totally takes O(log N) time
	void DecreaseKey(Vertex minHeap[], Vertex newNode, int hashTable[])
	{
		minHeap[hashTable[newNode.label]].value = newNode.value;

		int i = hashTable[newNode.label];
		Vertex temp;

		while (i > 1) {
			if (minHeap[i / 2].value > minHeap[i].value) {
				hashTable[minHeap[i].label] = i / 2;
				hashTable[minHeap[i / 2].label] = i;

				temp = minHeap[i / 2];
				minHeap[i / 2] = minHeap[i];
				minHeap[i] = temp;

				i = i / 2;
			} else {
				break;
			}
		}
	}

	//Removes and Returns the topmost element - O (log N)
	Vertex ExtractMin(Vertex minHeap[], int size, int hashTable[])
	{
		hashTable[minHeap[1].label] = size;
		hashTable[minHeap[size].label] = 1;

		Vertex min = minHeap[1];

		minHeap[1] = minHeap[size];
		--size;
		Heapify(minHeap, size, 1, hashTable);

		return min;
	}

	//The Dijkstra's Algorithm -> computes the shortest paths and fills them up in the shortestDistances array
	void dijkstra(Node adjacencyList[], int vertices, int startVertex, Path shortestDistances[]) {
		int i, j;
		Vertex minVertex = null;
		Vertex priorityQueue[] = new Vertex[vertices + 1];  // To use the array as 1-indexed

		int hashTable[] = new int[vertices + 1];    // Stores the location of Vi in hashTable[i] 

		// Initially no routes to vertices are know, so all are infinity
		for (i = 0; i <= vertices; ++i) {
			shortestDistances[i].distance = Integer.MAX_VALUE;
			shortestDistances[i].parent = -1;
			priorityQueue[i].label = i;
			priorityQueue[i].value = Integer.MAX_VALUE;
			hashTable[i] = priorityQueue[i].label;
		}

		// Setting distance to source to zero
		shortestDistances[startVertex].distance = 0;
		shortestDistances[startVertex].parent = 0;
		priorityQueue[startVertex].value = 0;
		minVertex.label = startVertex;
		minVertex.value = 0;

		BuildHeap(priorityQueue, vertices, hashTable);
		Node trav;

		while (vertices != 0) {     // Untill there are vertices to be processed in Queue
			minVertex = ExtractMin(priorityQueue, vertices, hashTable);   // Greedily process the nearest vertex
			--vertices;
			trav = adjacencyList[minVertex.label];    // Checking all the vertices adjacent to 'min'

			while (trav != null) {
				if (
						shortestDistances[minVertex.label].distance != Integer.MAX_VALUE &&
						shortestDistances[trav.vertex].distance > shortestDistances[minVertex.label].distance + trav.weight
						) {
					// We have discovered a new shortest route, make the neccesary adjustments in data
					shortestDistances[trav.vertex].distance = shortestDistances[minVertex.label].distance + trav.weight;
					shortestDistances[trav.vertex].parent = minVertex.label;

					Vertex changedVertex = null;

					changedVertex.label = trav.vertex;
					changedVertex.value = shortestDistances[trav.vertex].distance;

					DecreaseKey(priorityQueue, changedVertex, hashTable);
				}

				trav = trav.next;
			}
		}
	}

	void PrintShortestPath(Path shortestDistances[], int vertex, int startVertex)
	{
		if (vertex == startVertex) {
			System.out.println(" " + startVertex);
			return;
		} else {
			PrintShortestPath(shortestDistances, shortestDistances[vertex].parent, startVertex);
			System.out.println(" " + vertex);
		}
	}

	public static void main(String[] args) {
		int vertices, edges, i, j, v1, v2, w;
		Scanner s = new Scanner(System.in);

		System.out.println("Enter the Number of Vertices -\n");
		vertices = s.nextInt();

		System.out.println("Enter the Number of Edges -\n");
		edges = s.nextInt();

		Node adjacencyList[] = new Node[vertices + 1];  // to use the array as 1-indexed for simplicity

		for (i = 0; i <= vertices; ++i) {    // Must initialize your array
			adjacencyList[i] = null;
		}

		System.out.println("\n");
		DijsktraAlgo2 algo = new DijsktraAlgo2();

		for (i = 1; i <= edges; ++i) {
			v1 = s.nextInt();
			v2 = s.nextInt();
			w = s.nextInt();
			adjacencyList[v1] = algo.AddEdge(adjacencyList[v1], v2, w);
		}

		int startVertex;

		startVertex = s.nextInt();

		Path shortestDistances[] = new Path[vertices + 1];

		algo.dijkstra(adjacencyList, vertices, startVertex, shortestDistances);

		System.out.println("\nShortest Path from " +  startVertex + " to " +  vertices);
		algo.PrintShortestPath(shortestDistances, vertices /* The Last Vertex */, startVertex);
		System.out.println("\nTotal Path weight = " + shortestDistances[vertices] + "\n");

	}
}
