package com.narren.graph;

import java.util.Scanner;

public class DfsAdjanceyList {

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
		Node p = new DfsAdjanceyList().new Node();
		p.value = value;
		p.next = head;
		p.visited = false;
		return p;
	}
	
	public static void main(String[] args) {

		int vertices, edges, i, v, v1, v2;
		Scanner s = new Scanner(System.in);

		vertices = s.nextInt();
		edges = s.nextInt();
		/* This is the table of our Adjacency List
		 * Each element holds a Linked List
		 */
		Node[] adjanceyList = new Node[vertices + 1];

		//Each element of Parent Array holds the Node value of its parent
		int[] parent = new int[vertices + 1];


		for (i = 0; i <= vertices; ++i) {
			//Initialising our arrays
			adjanceyList[i] = null;
			parent[i] = -1;
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

		depth_first_search(adjanceyList, vertices, parent);

		System.out.println("\nParent Array -\n");
	    for (i = 1; i <= vertices; ++i) {
	        System.out.println("Parent of Vertex " + i + " = " + parent[i] + "\n");
	    }
	 
	
	}
	static void depth_first_search_explore(Node list[], int parent[], int vertex) {
	    if (parent[vertex] != -1) {
	        //un-visited vertex
	        Node temp = list[vertex];
	        if (temp != null) {
	        	temp.visited = true;
	        }
	 
	        //recursively visit all vertices accessible from this Vertex
	        while (temp != null) {
	 
	            if (parent[temp.value] == -1) {
	                parent[temp.value] = vertex;
	                //We started exploring from Vertex -'vertex',
	                //so the Vertex - temp->val, it's
	                //parent should be our initial vertex
	 
	                depth_first_search_explore(list, parent, temp.value);
	                //Then we recursively visit everything from the child vertex
	            } else {
	                //Checking if the edge is a Backward Edge
	                if (temp.visited) {
	                    System.out.println("\n" + vertex + " ---> " + temp.value + " is a Backward Edge\n");
	                }
	            }
	 
	            temp = temp.next;
	            //After finishing, move on to next Vertex adjacent to
	            //Vertex - 'vertex'
	        }
	 
	        if (temp != null) {
	        	temp.visited = false;
	        }
	    }
	}
	
	static void depth_first_search(Node list[], int length, int parent[]) {
	    int i;
	 
	    for (i = 1; i <= length; ++i) {
	 
	        if (parent[i] == -1) {
	            parent[i] = 0;
	            //It is a completely un-visited vertex and we start
	            //our DFS from here, so it has no parent, but just
	            //to mark it that we visited this node, we assign 0
	 
	            depth_first_search_explore(list, parent, i);
	            //By this we begin to explore all the vertices reachable
	            //from Vertex i
	        }
	    }
	}
	
}
