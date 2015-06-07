package com.narren.hackerRank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DijkstraAlgorithm {

	static List<Vertes> testNodes;
	static List<Edge> testEdges;
	public static void main(String[] args) {
		testNodes = new ArrayList<Vertes>();
		testEdges = new ArrayList<Edge>();
		for (int i = 0; i < 8; i++) {
			Vertes location = new Vertes("Node_" + i, "Node_" + i);
			testNodes.add(location);
		}

		addLane("Edge_0", 0, 1, 5);
		addLane("Edge_1", 0, 7, 8);
		addLane("Edge_2", 0, 4, 9);
		addLane("Edge_3", 2, 6, 11);
		addLane("Edge_4", 2, 3, 3);
		addLane("Edge_5", 3, 6, 9);
		addLane("Edge_6", 5, 2, 1);
		addLane("Edge_7", 5, 6, 13);
		addLane("Edge_8", 7, 2, 7);
		addLane("Edge_9", 7, 5, 6);
		addLane("Edge_10", 4, 7, 5);
		addLane("Edge_11", 4, 5, 4);
		addLane("Edge_12", 4, 6, 20);
		addLane("Edge_13", 1, 3, 15);
		addLane("Edge_14", 1, 2, 12);
		addLane("Edge_15", 1, 7, 4);

		// Lets check from location Loc_1 to Loc_10
		Graphs graph = new Graphs(testNodes, testEdges);
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		dijkstra.execute(testNodes.get(0));
		LinkedList<Vertes> path = dijkstra.getPath(testNodes.get(6));

		for (Vertes vertex : path) {
			System.out.println(vertex);
		}
	}

	private static void addLane(String laneId, int sourceLocNo, int destLocNo,
			int duration) {
		Edge lane = new Edge(laneId,testNodes.get(sourceLocNo), testNodes.get(destLocNo), duration);
		testEdges.add(lane);
	}


	private final List<Vertes> nodes;
	private final List<Edge> edges;
	private Set<Vertes> settledNodes;
	private Set<Vertes> unSettledNodes;
	private Map<Vertes, Vertes> predecessors;
	private Map<Vertes, Integer> distance;

	public DijkstraAlgorithm(Graphs graph) {
		// create a copy of the array so that we can operate on this array
		this.nodes = new ArrayList<Vertes>(graph.getVertexes());
		this.edges = new ArrayList<Edge>(graph.getEdges());
	}

	public void execute(Vertes source) {
		settledNodes = new HashSet<Vertes>();
		unSettledNodes = new HashSet<Vertes>();
		distance = new HashMap<Vertes, Integer>();
		predecessors = new HashMap<Vertes, Vertes>();
		distance.put(source, 0);
		unSettledNodes.add(source);
		while (unSettledNodes.size() > 0) {
			Vertes node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}

	private void findMinimalDistances(Vertes node) {
		List<Vertes> adjacentNodes = getNeighbors(node);
		for (Vertes target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node)
					+ getDistance(node, target)) {
				distance.put(target,
						getShortestDistance(node) + getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}

	}

	private int getDistance(Vertes node, Vertes target) {
		for (Edge edge : edges) {
			if (edge.getSource().equals(node)
					&& edge.getDestination().equals(target)) {
				return edge.getWeight();
			}
		}
		throw new RuntimeException("Should not happen");
	}

	private List<Vertes> getNeighbors(Vertes node) {
		List<Vertes> neighbors = new ArrayList<Vertes>();
		for (Edge edge : edges) {
			if (edge.getSource().equals(node)
					&& !isSettled(edge.getDestination())) {
				neighbors.add(edge.getDestination());
			}
		}
		return neighbors;
	}

	private Vertes getMinimum(Set<Vertes> vertexes) {
		Vertes minimum = null;
		for (Vertes vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}

	private boolean isSettled(Vertes vertex) {
		return settledNodes.contains(vertex);
	}

	private int getShortestDistance(Vertes destination) {
		Integer d = distance.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	/*
	 * This method returns the path from the source to the selected target and
	 * NULL if no path exists
	 */
	public LinkedList<Vertes> getPath(Vertes target) {
		LinkedList<Vertes> path = new LinkedList<Vertes>();
		Vertes step = target;
		// check if a path exists
		if (predecessors.get(step) == null) {
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		// Put it into the correct order
		Collections.reverse(path);
		return path;
	}
}

class Graphs {
	private final List<Vertes> vertexes;
	private final List<Edge> edges;

	public Graphs(List<Vertes> vertexes, List<Edge> edges) {
		this.vertexes = vertexes;
		this.edges = edges;
	}

	public List<Vertes> getVertexes() {
		return vertexes;
	}

	public List<Edge> getEdges() {
		return edges;
	}



} 

class Edge  {
	private final String id; 
	private final Vertes source;
	private final Vertes destination;
	private final int weight; 

	public Edge(String id, Vertes source, Vertes destination, int weight) {
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.weight = weight;
	}

	public String getId() {
		return id;
	}
	public Vertes getDestination() {
		return destination;
	}

	public Vertes getSource() {
		return source;
	}
	public int getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		return source + " " + destination;
	}


} 

class Vertes {
	final private String id;
	final private String name;


	public Vertes(String id, String name) {
		this.id = id;
		this.name = name;
	}
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertes other = (Vertes) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}

} 