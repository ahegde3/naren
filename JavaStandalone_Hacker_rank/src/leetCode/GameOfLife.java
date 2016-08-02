package leetCode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameOfLife {

	private Set<Coord> gameOfLife(Set<Coord> live) {
		Map<Coord,Integer> neighbours = new HashMap<>();
		for (Coord cell : live) {
			for (int i = cell.i-1; i<cell.i+2; i++) {
				for (int j = cell.j-1; j<cell.j+2; j++) {
					if (i==cell.i && j==cell.j) continue;
					Coord c = new Coord(i,j);
					if (neighbours.containsKey(c)) {
						neighbours.put(c, neighbours.get(c) + 1);
					} else {
						neighbours.put(c, 1);
					}
				}
			}
		}
		Set<Coord> newLive = new HashSet<>();
		for (Map.Entry<Coord,Integer> cell : neighbours.entrySet())  {
			if (cell.getValue() == 3 || cell.getValue() == 2 && live.contains(cell.getKey())) {
				newLive.add(cell.getKey());
			}
		}
		return newLive;
	}

	private static class Coord {
		int i;
		int j;
		private Coord(int i, int j) {
			this.i = i;
			this.j = j;
		}
		public boolean equals(Object o) {
			return o instanceof Coord && ((Coord)o).i == i && ((Coord)o).j == j;
		}
		public int hashCode() {
			int hashCode = 1;
			hashCode = 31 * hashCode + i;
			hashCode = 31 * hashCode + j;
			return hashCode;
		}
	}

	public void gameOfLife(int[][] board) {
		Set<Coord> live = new HashSet<>();
		int m = board.length;
		int n = board[0].length;
		for (int i = 0; i<m; i++) {
			for (int j = 0; j<n; j++) {
				if (board[i][j] == 1) {
					live.add(new Coord(i,j));
				}
			}
		};
		live = gameOfLife(live);
		for (int i = 0; i<m; i++) {
			for (int j = 0; j<n; j++) {
				board[i][j] = live.contains(new Coord(i,j))?1:0;
			}
		};

	}
	
	public static void main(String[] args) {
		int[][] board = new int[][]{
			{0, 0, 0, 0, 1, 1},
			{1, 1, 1, 0, 0, 0},
			{1, 0, 0, 1, 0, 0},
			{1, 0, 1, 0, 0, 0},
			{1, 0, 0, 1, 0, 1}
		};
		new GameOfLife().gameOfLife(board);
		
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 6; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}
}
