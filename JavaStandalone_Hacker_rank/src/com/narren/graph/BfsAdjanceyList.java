package com.narren.graph;

import java.util.Scanner;


class Node {
	int value;
	Node next;
}
public class BfsAdjanceyList {

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

	static void breadthFirstSearch(Node list[], int vertices, int parent[], int level[])
	{
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

		//Each element of Level Array holds the Level value of that node
		int[] level = new int[vertices + 1];

		for (i = 0; i <= vertices; ++i) {
			//Initialising our arrays
			adjanceyList[i] = null;
			parent[i] = 0;
			level[i] = -1;
		}

		for (i = 1; i <= edges; ++i) {
			v1 = s.nextInt();
			v2 = s.nextInt();
			adjanceyList[v1] = insert(adjanceyList[v1], v2);
			adjanceyList[v2] = insert(adjanceyList[v2], v1);
			/*For edge {v1, v2} = {2, 3},
			 *Adjacency List holds 2----3
			 *and 3-----2 as edges
			 */
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

		breadthFirstSearch(adjanceyList, vertices, parent, level);

		//Level Array
		System.out.println("\nLevel and Parent Arrays -\n");
		for (i = 1; i <= vertices; ++i) {
			System.out.println("Level of Node " + i + " is " + level[i] + ", Parent is " + parent[i] + "\n");
		}
	}
}
