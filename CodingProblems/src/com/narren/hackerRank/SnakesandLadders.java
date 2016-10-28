package com.narren.hackerRank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class SnakesandLadders {

	private List<Vertes> nodes;
	private List<Edge> edges;
	private Set<Vertes> settledNodes;
	private Set<Vertes> unSettledNodes;
	private Map<Vertes, Vertes> predecessors;
	private Map<Vertes, Integer> distance;
	static SnakesandLadders instance = new SnakesandLadders();
	static List<Edge> testEdges = null;

	public static void main(String[] args) {
		/**
1
3
6 80
8 55
13 99
1
56 12
		 */
		Scanner in = new Scanner(System.in);
		int T = in.nextInt();
		while (T > 0) {
			int L = in.nextInt();
			Vertes[] testNodes = new Vertes[101];
			//List<Vertes> testNodes = new ArrayList<Vertes>(101);
			testEdges = new ArrayList<Edge>();
			SnakesandLadders.Vertes source = instance.new Vertes(1, "Node_1", false);
			SnakesandLadders.Vertes destination = instance.new Vertes(100, "Node_100", false);
			testNodes[1] = source;
			testNodes[100] = destination;
			while (L > 0) {
				int start = in.nextInt();
				int end = in.nextInt();	
				testNodes[start] = instance.new Vertes(start, "Node_" + start, false);
				testNodes[end] = instance.new Vertes(end, "Node_" + end, false);
				SnakesandLadders.Edge lane1 = instance.new Edge("Edge_" + start +  "_" + end, testNodes[start], testNodes[end], 0, false);
				SnakesandLadders.Edge lane2 = instance.new Edge("Edge_1" +  "_" + start, testNodes[1], testNodes[start], (start - 1), false);
				SnakesandLadders.Edge lane3 = instance.new Edge("Edge_" + end +  "_100", testNodes[end], testNodes[100], (100 - end), false);
				testEdges.add(lane1);
				testEdges.add(lane2);
				testEdges.add(lane3);
				L--;
			}
			int S = in.nextInt();
			while (S > 0) {
				int start = in.nextInt();
				int end = in.nextInt();	
				testNodes[start] = instance.new Vertes(start, "Node_" + start, true);
				testNodes[end] = instance.new Vertes(end, "Node_" + end, false);
				SnakesandLadders.Edge lane1 = instance.new Edge("Edge_" + start +  "_" + end, testNodes[start], testNodes[end], 0, true);
				SnakesandLadders.Edge lane2 = instance.new Edge("Edge_1" +  "_" + end, testNodes[1], testNodes[end], (end - 1), false);
				//SnakesandLadders.Edge lane3 = instance.new Edge("Edge_" + start +  "_100", testNodes[start], testNodes[100], (100 - start), false);
				testEdges.add(lane1);
				testEdges.add(lane2);
				//testEdges.add(lane3);
				S--;
			}
			
			List<Vertes> nodes = Arrays.asList(testNodes);
			SnakesandLadders.Graphs graph = instance.new Graphs(nodes, testEdges);
			instance.nodes = new ArrayList<Vertes>(graph.getVertexes());
			instance.edges = new ArrayList<Edge>(graph.getEdges());
			instance.connectGaps(testNodes);
			graph.setEdges(testEdges);
			instance.execute(testNodes[1]);
			//LinkedList<Vertes> path = instance.getPath(testNodes[100]);
			System.out.println(instance.getPath(testNodes[100]));
			T--;
		}
	}
	
	public void connectGaps(Vertes[] testNodes) {
		Vertes previous = null;
		for (int i = 0; i < testNodes.length; i++) {
			if (i > 1 && testNodes[i] != null) {
				if (previous != null) {
					//System.out.println(previous);
					for (Edge edge : testEdges) {
						//System.out.println(" " + edge.getSource());
						Vertes source = (edge.isSnake == true) ? edge.getDestination() : edge.getSource();
						Vertes destination = (edge.isSnake == true) ? edge.getSource() : edge.getDestination();
						if (source.equals(previous)) {
							if (!destination.equals(testNodes[i]) && !previous.isSnake) {
								// add an edge
								SnakesandLadders.Edge lane2 = instance.new Edge("Edge_" + previous.getId() + "_" + testNodes[i].getId(),
										previous, testNodes[i], (testNodes[i].getId() - previous.getId()), false);
								testEdges.add(lane2);
								//System.out.println("  " + previous + "-" + testNodes[i]);
								
								previous = testNodes[i];
								break;
							}
						}
					}
					previous = testNodes[i];
				} else {
					previous = testNodes[i];
				}
			}
		}
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
		for (Edge edge : testEdges) {
			if (edge.getSource().equals(node)
					&& edge.getDestination().equals(target)) {
				return edge.getWeight();
			}
		}
		throw new RuntimeException("Should not happen");
	}

	private List<Vertes> getNeighbors(Vertes node) {
		List<Vertes> neighbors = new ArrayList<Vertes>();
		for (Edge edge : testEdges) {
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
	public /*LinkedList<Vertes>*/int getPath(Vertes target) {
		LinkedList<Vertes> path = new LinkedList<Vertes>();
		Vertes step = target;
		int steps = -1;
		// check if a path exists
		if (predecessors.get(step) == null) {
			return 0;//return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			for (Edge edge : testEdges) {
				if (edge.getSource().equals(predecessors.get(step))
						&& edge.getDestination().equals(step)) {
					 int weight = edge.getWeight();
					 if (weight <= 6 && weight > 0) {
						 steps++;
					 } else {
						 if (weight % 6 == 0) {
							 steps += weight/6;
						 } else {
							 steps += weight/6 +1;
						 }
					 }
				}
			}
			step = predecessors.get(step);
			path.add(step);
		}
		// Put it into the correct order
		Collections.reverse(path);
		return (steps == -1) ? -1 : steps + 1;//return path;
	}

	class Graphs {
		private final List<Vertes> vertexes;
		private List<Edge> edges;

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
		
		public void setEdges(List<Edge> edge) {
			this.edges = edge;
		}



	} 

	class Edge  {
		private final String id; 
		private final Vertes source;
		private final Vertes destination;
		private final int weight; 
		private final boolean isSnake;

		public Edge(String id, Vertes source, Vertes destination, int weight, boolean isSnake) {
			this.id = id;
			this.source = source;
			this.destination = destination;
			this.weight = weight;
			this.isSnake = isSnake;
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
		final private int id;
		final private String name;
		final private boolean isSnake;


		public Vertes(int id, String name, boolean isSnake) {
			this.id = id;
			this.name = name;
			this.isSnake = isSnake;
		}
		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + id;
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
			if (id == 0) {
				if (other.id != 0)
					return false;
			} else if (id != other.id)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return name;
		}

	} 
}
