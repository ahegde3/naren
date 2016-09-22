package sotong;

import java.util.Scanner;

/**
 * 
 * Refer AutomaticFuelingRobot_X.jpg for problem description
 * 
Sample Input
5
3
2 1 2
5
1 2 1 2 1
5
2 1 1 2 1
8
2 2 1 1 1 2 1 1
8
2 2 2 2 2 2 2 2

===Expected
7
12
14
35
41
 * 
 * @author nsbisht
 *
 */
public class AutomaticFuelingRobot {

	static State[] states;
	static int writeIndex = 0;
	static int readIndex = 0;
	static boolean[] visited;
	
	static class Fuel {
		int type; // 1: G, 2: D
		int capa;
		
		public Fuel(int t, int c) {
			type = t;
			capa = c;
		}
	}
	
	static class State {
		int curIndx;
		int type; // 1: G, 2: D
		int steps;
		State parent;
		int gLeft;
		int dLeft;
		Fuel fuel;
		
		public State(int c, int t, int s, State p, int g, int d, Fuel f) {
			curIndx = c;
			type = t;
			steps = s;
			parent = p;
			gLeft = g;
			dLeft = d;
			fuel = f;
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while(T > 0) {
			int N = sc.nextInt();
			int[] input = new int[N + 2];
			int dStat = N + 1;
			input[0] = 1;
			input[dStat] = 2;
			states = new State[N * 10000];
			visited = new boolean[300000];
			int g = 0;
			int d = 0;
			for(int i = 1; i < N + 1; i++) {
				input[i] = sc.nextInt();
				if(input[i] == 1) {
					g++;
				} else {
					d++;
				}
			}
			int steps = process(input, dStat, g, d);
			System.out.println(steps);
			T--;
		}
	}
	
	static int getVisitedIndex(State s) {
		return Integer.parseInt("" + s.type + s.curIndx + s.gLeft + s.dLeft + s.fuel.type + s.fuel.capa);
	}
	
	static int process(int[] input, int dStat, int g, int d) {
		int minSteps = 0;
		State initial = new State(0, 1, 0, null, g, d, new Fuel(1, 2));
		states[writeIndex++] = initial;
		while(states[readIndex] != null) {
			State s = states[readIndex++];
			System.out.println("curIn=" + s.curIndx + " type=" + s.type + " gLeft=" + s.gLeft + " dLeft="  + s.dLeft +" steps=" + s.steps);
			if(s.gLeft == 0 && s.dLeft == 0) {
				return s.steps - 1;
			}
			int visitedIndex = getVisitedIndex(s);
			visited[visitedIndex] = true;
			if(s.curIndx == 0 || s.curIndx == dStat) {
				State[] neighbors = getNextState(s, input, dStat);
				for(State st : neighbors) {
					visitedIndex = getVisitedIndex(st);
					if(!visited[visitedIndex])
						states[writeIndex++] = st;
				}
			} else {
				if(s.fuel.type == input[s.curIndx] && s.fuel.capa > 0) {
					// WIthout consuming
					State[] neighbors = getNextState(s, input, dStat);
					for(State st : neighbors) {
						visitedIndex = getVisitedIndex(st);
						if(!visited[visitedIndex])
							states[writeIndex++] = st;
					}
					
					//With Consuming
					s.fuel.capa--;
					if(s.type == 1) {
						s.gLeft--;
					} else {
						s.dLeft--;
					}
					if(s.gLeft >=0 && s.dLeft >=0) {
						State[] neighbor = getNextState(s, input, dStat);
						for(State st : neighbor) {
							visitedIndex = getVisitedIndex(st);
							if(!visited[visitedIndex])
								states[writeIndex++] = st;
						}
					}
					
				} else {
					State[] neighbors = getNextState(s, input, dStat);
					for(State st : neighbors) {
						visitedIndex = getVisitedIndex(st);
						if(!visited[visitedIndex])
							states[writeIndex++] = st;
					}
				}
			}
		}
		return minSteps;
	}
	
	static State[] getNextState(State state, int[] input, int dStat) {
		State[] neighbors;
		if(state.curIndx == 0 || state.curIndx == dStat) {
			neighbors = new State[1];
			if(state.curIndx == 0) {
				neighbors[0] = new State(1, input[1], state.steps + 1, state, state.gLeft, state.dLeft, new Fuel(1, 2));
			} else {
				neighbors[0] = new State(dStat - 1, input[dStat - 1], state.steps + 1, state, state.gLeft, state.dLeft, new Fuel(2, 2));
			}
		} else {
			neighbors = new State[2];
			neighbors[0] = new State(state.curIndx - 1, input[state.curIndx - 1], state.steps + 1, state, state.gLeft, state.dLeft, state.fuel);
			neighbors[1] = new State(state.curIndx + 1, input[state.curIndx + 1], state.steps + 1, state, state.gLeft, state.dLeft, state.fuel);
		}
		return neighbors;
	}
}
