package com.narren.gayle.treeAndGraph;

public class TreeAndGraphQ1 {

	/**
	 * Find out if there is a route between two nodes of a Graph
	 * @param a
	 * @param b
	 * @return
	 */
	GraphNode getRoute(GraphNode a, GraphNode b) {
		if(a == null || b == null || a.visited) {
			return null;
		}
		if(a.value == b.value) {
			return a;
		}
		
		a.visited = true;
		
		GraphNode ret = null;
		
		for(GraphNode n : a.neighbors) {
			ret = getRoute(n, b);
		}
		
		return ret;
	}
	
	public static void main(String[] args) {
		
	}
}
