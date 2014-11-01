class State {
	int m; // number of missionaries on the left bank;
	int c; // number of cannibals on the left bank;
	int b; // 0 for boat on left bank, 1 for boat on the right;

	State() { // construct the start state (3,3,L)
		m = 3;
		c = 3;
		b = 0;
	}

	State(State s) {
		m = s.m;
		c = s.c;
		b = s.b;
	}

	boolean legal()
	/* test legality of this */
	{
		if (m < 0 || m > 3 || c < 0 || c > 3)
			return false;
		if (3 - m < 0 || 3 - m > 3 || 3 - c < 0 || 3 - c > 3)
			return false;
		if (m > 0 && c > m)
			return false;
		if (3 - m > 0 && 3 - c > 3 - m)
			return false;
		return true;
	}

	/**
	 * return state resulting from this by moving Mis missionaries and Can
	 * cannibals across the river.
	 */
	State move(int Mis, int Can) {
		State ans = new State(this);
		if (b == 0) {
			ans.m = m - Mis;
			ans.c = c - Can;
			ans.b = 1;
		} else {
			ans.m = m + Mis;
			ans.c = c + Can;
			ans.b = 0;
		}
		return ans;
	}

	void display()
	/* display this on the screen */
	{
		System.out.println(m + " " + c + " " + (b == 0 ? 'L' : 'R'));
	}
}

public class MissionariesAndCannibals {
	static boolean already[][][] = new boolean[4][][];
	static State pred[][][] = new State[4][][];
	static int d[][][] = new int[4][][];
	static int infinity = 20000;

	/*
	 * return the minimum "distance" from s to the goal state (0,0,R). Here
	 * "distance" is the number of river crossings. Assumes already and pred are
	 * initialized and already[a][b][c] contains true if state (a,b,c) has been
	 * "visited", i.e. go has already been called on it, and in that case,
	 * pred[a][b][c] is the state from which we called go on state (a,b,c).
	 * Store the distance from s to goal in d[s.m][s.c][s.b]
	 */
	static int go(State s) { /* stopping condition is when s is the goal state */
		System.out.println("Trying " + s.m + s.c + (s.b == 0 ? 'L' : 'R'));
		already[s.m][s.c][s.b] = true;
		if (s.m == 0 && s.c == 0 && s.b == 1) {
			d[0][0][1] = 0;
			return 0; // Success!
		}
		// Now generate the neighbors of state s
		int i, j;
		int ans = infinity;
		int z;
		State t;
		// the bug in class was that the following line had <2 instead of <= 2
		for (i = 0; i <= 2; i++)
			for (j = 0; j <= 2; j++) {
				if (i + j == 0)
					continue;
				if (i + j > 2)
					continue;
				t = s.move(i, j);
				if (!t.legal())
					continue;
				if (already[t.m][t.c][t.b])
					z = d[t.m][t.c][t.b];
				else
					z = go(t);
				if (z < ans) {
					ans = z;
					pred[t.m][t.c][t.b] = s;
				}
			}
		// Now, ans is the shortest distance to goal from
		// any neighbor of s.
		System.out.println("Returning " + ans + " from go");
		if (ans < infinity) {
			d[s.m][s.c][s.b] = ans + 1;
			return ans + 1;
		} else
			return infinity;
	}

	/**
	 * Solve the missionaries and cannibals problem by depth-first search.
	 * 
	 * @return the number of river crossings required. The solution itself can
	 *         be reconstructed using the pred array.
	 */
	public static void main(String args[]) { // first initialize already and
												// pred
		int i, j, k;
		for (i = 0; i < 4; i++) {
			already[i] = new boolean[4][];
			d[i] = new int[4][];
			pred[i] = new State[4][];
			for (j = 0; j < 4; j++) {
				already[i][j] = new boolean[2];
				d[i][j] = new int[2];
				pred[i][j] = new State[2];
				for (k = 0; k < 2; k++) {
					already[i][j][k] = false;
					pred[i][j][k] = new State();
					d[i][j][k] = infinity;
				}
			}
		}
		// that completes the initialization
		State start = new State(); // constructs (3,3,L)
		int ans = go(start);
		if (ans == infinity) {
			System.out
					.println("No solution.  You moron, you programmed it wrong.");
			// of course if we pretend we didn't know it had a solution,
			// we might have needed some code here.
			return;
		}
		// problem solved!
		// Let's print out the solution.
		System.out.println("Success:  it takes " + ans + " crossings.");
		State q = new State();
		q.m = 0;
		q.c = 0;
		q.b = 1; // goal state (0,0,R)
		State[] solution = new State[ans + 1];
		solution[ans] = q;
		for (i = ans - 1; i >= 0; i--) {
			solution[i] = pred[q.m][q.c][q.b];
			q = solution[i];
		}
		// solution[0] better be (3,3,L)
		for (i = 0; i < ans; i++)
			solution[i].display();
	}
}
