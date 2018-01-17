package com.narren.gayle.treeAndGraph;

public class TreeAndGraphQ1 {

	GraphNode getRoute(GraphNode a, GraphNode b) {
		if(a == null || b == null) {
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
