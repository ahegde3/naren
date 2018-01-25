package com.narren.gayle.treeAndGraph;

public class TreeAndGraphQ1 {

	/**
	 * Find out if there is a route between two nodes of a Graph
	 * @param a
	 * @param b
	 * @return
	 */
	Graph getRoute(Graph a, Graph b) {
		if(a == null || b == null || a.visited) {
			return null;
		}
		if(a.value == b.value) {
			return a;
		}
		
		a.visited = true;
		
		Graph ret = null;
		
		for(Graph n : a.neighbors) {
			ret = getRoute(n, b);
		}
		
		return ret;
	}
	
	public static void main(String[] args) {
		
	}
}
