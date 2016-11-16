package com.narren.sotong;

import java.util.Scanner;

import sun.misc.Queue;
/**
 * 
3
15
0 2 2 0 1 2 2 1 0 0 0 0 2 0 2
6
0 0 0 0 0 0
10
0 2 0 0 1 0 0 0 0 0
 * 
 * @author nsbisht
 *
 */
public class Samthello {

	static class State {
		int[] game;
		int turns;
		
		public State(int[] in, int t) {
			game = in;
			turns = t;
		}
	}
	
	static void checkRule2(int[] game, int curIndex, int player) {
		// 1 --> B
		// 2 --> W
		// first go right
		for(int i = curIndex + 1; i < game.length; i++) {
			if(game[i] == 0) {
				break;
			}
			if(game[i] == player) {
				int temp = curIndex;
				while(temp <= i) {
					game[temp] = player;
					temp++;
				}
				break;
			}
		}
		
		// try left
		for(int i = curIndex - 1; i >= 0; i--) {
			if(game[i] == 0) {
				break;
			}
			if(game[i] == player) {
				int temp = curIndex;
				while(temp >= i) {
					game[temp] = player;
					temp--;
				}
				break;
			}
		}
	}
	
	static void checkRule3(int[] game, int curIndex, int player) {
		// first go right
		for(int i = curIndex + 1; i < game.length; i++) {
			if(game[i] == 0 || game[i] == player) {
				break;
			}
			if(i == game.length - 1) {
				int temp = curIndex;
				while(temp <= i) {
					game[temp] = player;
					temp++;
				}
				break;
			}
		}

		// try left
		for(int i = curIndex - 1; i >= 0; i--) {
			if(game[i] == 0 || game[i] == player) {
				break;
			}
			if(i == 0) {
				int temp = curIndex;
				while(temp >= i) {
					game[temp] = player;
					temp--;
				}
				break;
			}
		}
	}
	
	static int countBlacks(int[] in) {
		int bCounts = 0;
		for(int i : in) {
			if(i == 1) {
				bCounts++;
			}
		}
		return bCounts;
	}
	
	static int process(int[] initGame) throws InterruptedException {
		int maxPoints = 0;
		Queue<State> queue = new Queue<State>();
		queue.enqueue(new State(initGame, 0));
		while(!queue.isEmpty()) {
			State s = queue.dequeue();
			State[] possibilities = getPossibleStates(s);
			for(State state : possibilities) {
				if(state.turns == 3) {
					maxPoints = Math.max(maxPoints, countBlacks(state.game));
				}
				queue.enqueue(state);
			}
			
		}
		return maxPoints;
	}
	
	static int[] getNewArr(int[] in) {
		int[] res = new int[in.length];
		for(int i = 0; i < in.length; i++) {
			res[i] = in[i];
		}
		return res;
	}
	
	static int insertWhite(int[] in) {
		for(int i = 0; i < in.length; i++) {
			if(in[i] == 0) {
				in[i] = 2;
				return i;
			}
		}
		return -1;
	}
	
	static State[] getPossibleStates(State init) {
		State[] states;
		int possibleStates = 0;
		for(int i = 0; i < init.game.length; i++) {
			if(init.game[i] == 0) {
				possibleStates++;
			}
		}
		states = new State[possibleStates];
		int insertIndex = 0;
		for(int i = 0; i < init.game.length; i++) {
			if(init.game[i] == 0) {
				int[] newGame = getNewArr(init.game);
				newGame[i] = 1; // Adding black
				checkRule2(newGame, i, 1);
				checkRule3(newGame, i, 1);
				int index = insertWhite(newGame);
				if(index >= 0) {
					checkRule2(newGame, index, 2);
					checkRule3(newGame, index, 2);					
				}
				states[insertIndex++] = new State(newGame, init.turns + 1);
			}
		}
		return states;
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while(T > 0) {
			int N = sc.nextInt();
			int[] initState = new int[N];
			for(int i = 0; i < N; i++) {
				initState[i] = sc.nextInt();
			}
			int max = process(initState);
			System.out.println(max);
			T--;
		}
	}
}
