package com.narren.hackerRank;

import java.util.Scanner;

public class InspectionOfWaterPipes {

	/**
	 * 
1
5 6 2 1 3
0 0 5 3 6 0
0 0 2 0 2 0
3 3 1 3 7 0
0 0 0 0 0 0
0 0 0 0 0 0
	 */
	static boolean[][] visited;
	static int[][] arr;
	static int M;
	static int N;
	static int R;
	static int C;
	static int L;
	static int count;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while(T > 0) {
			N = sc.nextInt();
			M = sc.nextInt();
			R = sc.nextInt();
			C = sc.nextInt();
			L = sc.nextInt();
			arr = new int[N][M];
			visited = new boolean[N][M];
			for(int i = 0; i < N; i++) {
				for(int j = 0; j < M; j++) {
					arr[i][j] = sc.nextInt();
				}
			}
			process(R, C, L);
			T--;
		}
	}

	static int process(int r, int c, int lev) {
		if(r < 0 || c < 0) {
			return 0;
		}
		if(visited[r][c]) {
			return 0;
		}
		if(lev < 1) {
			return 0;
		}
		//First move to left
		c = canMove(r, c, r, c - 1) ? process(r, c - 1, lev - 1) : 0;
		if(!visited[r][c]) {
			visited[r][c] = true;
			count++;
		}

		//Now move to up
		r = canMove(r, c, r - 1, c) ? process(r - 1, c, lev - 1) : 0;
		if(!visited[r][c]) {
			visited[r][c] = true;
			count++;
		}

		//Move right
		c = canMove(r, c, r , c + 1) ? process(r, c + 1, lev - 1) : 0;
		if(!visited[r][c]) {
			visited[r][c] = true;
			count++;
		}

		//Move down
		c = canMove(r, c, r + 1 , c) ? process(r + 1, c, lev - 1) : 0;
		if(!visited[r][c]) {
			visited[r][c] = true;
			count++;
		}

		return r;
	}

	static boolean canMove(int cr, int cc, int nr, int nc) {
		if (nr < 0 || nc < 0) {
			return false;
		}
		int cp = arr[cr][cc];
		int np = arr[nr][nc];
		if(np == 0) {
			return false;
		}
		switch (cp) {
		case 1:
			if(cr == nr && cc == nc + 1) {
				//left cell
				if (np == 2 || np == 6 || np == 7) {
					return false;
				} else {
					return true;
				}
			} else if (cc == nc && cr == nr + 1) {
				// up cell
				if (np == 3 || np == 4 || np == 7) {
					return false;
				} else {
					return true;
				}
			} else if (cr == nr && cc == nc - 1) {
				// right cell
				if (np == 2 || np == 4 || np == 5) {
					return false;
				} else {
					return true;
				}
			} else if (cc == nc && cr == nr - 1) {
				// down cell
				if (np == 3 || np == 5 || np == 6) {
					return false;
				} else {
					return true;
				}
			}
			break;

		case 2:
			if(cr == nr && cc == nc + 1) {
				//left cell
				return false;
			} else if (cc == nc && cr == nr + 1) {
				// up cell
				if (np == 3 || np == 4 || np == 7) {
					return false;
				} else {
					return true;
				}
			} else if (cr == nr && cc == nc - 1) {
				// right cell
				return false;
			} else if (cc == nc && cr == nr - 1) {
				// down cell
				if (np == 3 || np == 5 || np == 6) {
					return false;
				} else {
					return true;
				}
			}

			break;

		case 3:
			if(cr == nr && cc == nc + 1) {
				//left cell
				if (np == 2 || np == 6 || np == 7) {
					return false;
				} else {
					return true;
				}
			} else if (cc == nc && cr == nr + 1) {
				// up cell
				return false;
			} else if (cr == nr && cc == nc - 1) {
				// right cell
				if (np == 2 || np == 4 || np == 5) {
					return false;
				} else {
					return true;
				}
			} else if (cc == nc && cr == nr - 1) {
				// down cell
				return false;
			}

			break;

		case 4:
			if(cr == nr && cc == nc + 1) {
				//left cell
				return false;
			} else if (cc == nc && cr == nr + 1) {
				// up cell
				if (np == 3 || np == 7) {
					return false;
				} else {
					return true;
				}
			} else if (cr == nr && cc == nc - 1) {
				// right cell
				if (np == 2 || np == 5) {
					return false;
				} else {
					return true;
				}
			} else if (cc == nc && cr == nr - 1) {
				// down cell
				return false;
			}

			break;

		case 5:
			if(cr == nr && cc == nc + 1) {
				//left cell
				return false;
			} else if (cc == nc && cr == nr + 1) {
				// up cell
				return false;
			} else if (cr == nr && cc == nc - 1) {
				// right cell
				if (np == 2 || np == 4) {
					return false;
				} else {
					return true;
				}
			} else if (cc == nc && cr == nr - 1) {
				// down cell
				if (np == 3 || np == 6) {
					return false;
				} else {
					return true;
				}
			}

			break;

		case 6:
			if(cr == nr && cc == nc + 1) {
				//left cell
				if (np == 2 || np == 7) {
					return false;
				} else {
					return true;
				}
			} else if (cc == nc && cr == nr + 1) {
				// up cell
				return false;
			} else if (cr == nr && cc == nc - 1) {
				// right cell
				return false;
			} else if (cc == nc && cr == nr - 1) {
				// down cell
				if (np == 3 || np == 5) {
					return false;
				} else {
					return true;
				}
			}

			break;

		case 7:
			if(cr == nr && cc == nc + 1) {
				//left cell
				if (np == 2 || np == 6) {
					return false;
				} else {
					return true;
				}
			} else if (cc == nc && cr == nr + 1) {
				// up cell
				if (np == 3 || np == 4) {
					return false;
				} else {
					return true;
				}
			} else if (cr == nr && cc == nc - 1) {
				// right cell
				return false;
			} else if (cc == nc && cr == nr - 1) {
				// down cell
				return false;
			}

			break;
		}
		return false;

	}

}
