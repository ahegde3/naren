package leetCode;

public class GameOfLife {

	int[] movesX = new int[]{0, 0, -1, 1, -1, 1, 1, -1};
	int[] movesY = new int[]{1, -1, 0, 0, 1, 1, -1, -1};

	int[] getNeighbors(int[][] board, int r, int c, int R, int C) {
		int[] neighbors = new int[]{-1, -1, -1, -1, -1, -1, -1, -1};
		int index = 0;
		for(int i = 0; i < 8; i++) {
			int xVal = r + movesX[i];
			int yVal = c + movesY[i];
			if(xVal < 0 || yVal < 0 || xVal >= R || yVal >= C) {
				continue;
			}
			neighbors[index++] = board[xVal][yVal];
		}
		return neighbors;

	}

	public void gameOfLife(int[][] board) {
		int R = board.length;
		int C = board[0].length;
		for(int i = 0; i < R; i++) {
			for(int j = 0; j < C; j++) {
				int[] neighbors = getNeighbors(board, i, j, R, C);
				int k = 0;
				int ones = 0;
				while(k < 8 && neighbors[k] != -1) {
					if(neighbors[k] == 1) {
						ones++;
					}
					k++;
				}
				if(ones < 2 || ones > 3) {
					if((board[i][j] & 1) == 1) {
						board[i][j] = 3;
					}
					
				} else if(ones == 3) {
					if((board[i][j] & 1) == 0) {
						board[i][j] = 2;
					}
				}
			}
		}
		
		for(int i = 0; i < R; i++) 
			for(int j = 0; j < C; j++) {
				if(board[i][j] == 3) {
					board[i][j] = 0;
				}
				if(board[i][j] == 2) {
					board[i][j] = 1;
				}
			}
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
