package com.narren.graph;

import java.util.Scanner;

/*class Node {
	int value;
	Node next;
}*/

/*
 * Snake and Ladder Shortest Path
 * using Adjacency List and
 * Breadth First Search.
 */

public class SnakesandLaddersAdjanceyList {

	/**
	 * This is exactly as Head Insertion of a Linked List.
       We choose Head Insertion to get an O(1) Insertion
       into an Adjacency List, Tail Insertion can also be used
	 * @param head
	 * @param value
	 * @return
	 */
	private static Node insert (Node head, int value) {
		Node p = new Node();
		p.value = value;
		p.next = head;
		return p;
	}

	static void breadthFirstSearch(Node list[], int vertices, int parent[], int level[]) {
		Node temp;
		int i, par, lev;
		boolean flag = true;
		//'lev' represents the level to be assigned
		//'par' represents the parent to be assigned
		//'flag' used to indicate if graph is exhausted

		lev = 0;
		level[1] = lev;
		/* We start from node - 1
		 * So, Node - 1 is at level 0
		 * All immediate neighbours are at
		 * level 1 and so on.
		 */

		while (flag) {
			flag = false;
			for (i = 1; i <= vertices; ++i) {
				if (level[i] == lev) {
					flag = true;
					temp = list[i];
					par = i;

					while (temp != null) {
						if (level[temp.value] != -1) {
							temp = temp.next;
							continue;
						}

						level[temp.value] = lev + 1;
						parent[temp.value] = par;
						temp = temp.next;
					}
				}
			}

			++lev;
		}
	}

	static void replace(Node head, int num, int replacedNum) {
		Node p = head;

		while (p.next != null) {
			if (p.value == num) {
				break;
			}

			p = p.next;
		}

		p.value = replacedNum;
	}

	public static void main(String[] args) {

		Scanner s = new Scanner(System.in);

		int T = s.nextInt();

		while (T > 0) {

			int vertices, edges, i, j, v, v1, v2;
			vertices = 100;
			edges = 0;
			/* This is the table of our Adjacency List
			 * Each element holds a Linked List
			 */
			Node[] adjanceyList = new Node[vertices + 1];

			//Each element of Parent Array holds the Node value of its parent
			int[] parent = new int[vertices + 1];

			//Each element of Level Array holds the Level value of that node
			int[] level = new int[vertices + 1];

			for (i = 0; i <= vertices; ++i) {
				//Initialising our arrays
				adjanceyList[i] = null;
				parent[i] = 0;
				level[i] = -1;
			}

			for (i = 1; i <= vertices; ++i) {
				for (j = 1; j <= 6 && j + i <= 100; ++j) {
					adjanceyList[i] = insert(adjanceyList[i], i + j);
					++edges;
				}
			}
			System.out.println("Edges added so far = " + edges +"\n");

			int numLadders, numSnakes;

			System.out.println("Enter the Number of Ladders and Snakes -\n");
			numLadders = s.nextInt();


			//System.out.println("Enter the Ladder Edges -\n");
			//Dealing with Ladder Edges
			for (i = 0; i < numLadders; ++i) {
				v1 = s.nextInt();
				v2 = s.nextInt();

				j = v1 - 6;

				if (j < 1) {
					j = 1;
				}

				for (; j < v1; ++j) {
					replace(adjanceyList[j], v1, v2);
				}
			}

			//System.out.println("Enter the Snake Edges -\n");
			numSnakes = s.nextInt();
			//Dealing with Snakes Edges
			for (i = 0; i < numSnakes; ++i) {
				v1 = s.nextInt();
				v2 = s.nextInt();

				j = v1 - 6;

				if (j < 0) {
					j = 0;
				}

				for (; j < v1; ++j) {
					//Replacing Vertex value v1 by v2
					replace(adjanceyList[j], v1, v2);
				}
			}

			//Printing Adjacency List
			System.out.println("\nAdjacency List -\n");
			for (i = 1; i <= vertices; ++i) {
				System.out.println(i + " -> ");

				Node temp = adjanceyList[i];

				while (temp != null) {
					System.out.println(temp.value + " -> ");
					temp = temp.next;
				}

				System.out.println("NULL\n");
			}

			breadthFirstSearch(adjanceyList, vertices, parent, level);

			System.out.println("\nNumber of Moves required = " + level[vertices] + "\n");

			/*Just a little mechanism to print the shortest path*/
			int[] path = new int[level[vertices] + 1];

			j = 100;
			path[0] = j;

			for (i = 1; i <= level[vertices]; ++i) {
				path[i] = parent[j];
				j = parent[j];
			}

			System.out.println("\nShortest Path -\n");
			for (i = level[vertices]; i >= 0; --i) {
				System.out.print(path[i] + " -> ");
			}
			System.out.println("Finish...!\n");
			T--;
		}

	}
}
