package com.narren.hackerRank;

import java.util.Scanner;
import java.util.Stack;

class Vertex {
	public char label;
	public boolean visited;
	public Vertex (/*char lab*/) {
		//label = lab;
		visited = false;
	}
}
public class Graph {
	private Vertex vertexList[];
	private int adjMatrix[][];
	private int vertexCount;
	private Stack theStack;
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int verticesCount = in.nextInt();
		int edgesCount = in.nextInt();
		Graph graph = new Graph(verticesCount);
		for (int i = 0; i < edgesCount; i++) {
			int start = in.nextInt();
			int end = in.nextInt();
			graph.addEdge(start - 1, end - 1);
		}
		graph.dfs();
		
	}
	public Graph(int vertices) {
		vertexList = new Vertex[vertices];
		adjMatrix = new int[vertices][vertices];
		vertexCount = vertices;
		for (int y = 0; y < vertices; y++) {
			for (int x = 0; x < vertices; x++) {
				adjMatrix[x][y] = 0;
			}
		}
		for (int x = 0; x < vertices; x++) {
			vertexList[x] = new Vertex();
		}
		theStack = new Stack<Integer>();
	}
	
	public void addVertix(/*char lab*/) {
		vertexList[vertexCount++] = new Vertex(/*lab*/);
	}
	
	public void addEdge(int start, int end) {
		adjMatrix[start][end] = 1;
		adjMatrix[end][start] = 1;
	}
	
	public void displayVertex(int v) {
		System.out.println(v/*vertexList[v].label*/);
	}
	
	public void dfs() {
		vertexList[0].visited = true;
		displayVertex(0);
		theStack.push(0);
		
		while (!theStack.isEmpty()) {
			// get an unvisited vertex adjacent to stack top
			int v = getAdjUnvisitedVertex((int) theStack.peek());
			if(v == -1) {
				theStack.pop();
			} else {
				vertexList[v].visited = true;
				displayVertex(v);
				theStack.push(v);
			}
		}
		for (int j = 0; j < vertexCount; j++) { // reset flags
			vertexList[j].visited = false;
		}
	}
	
	public int getAdjUnvisitedVertex(int v) {
		for (int i = 0; i < vertexCount; i++) {
			if (adjMatrix[v][i] == 1 && vertexList[i].visited == false) {
				return i;
			}
		}
		return -1;
	}

}
