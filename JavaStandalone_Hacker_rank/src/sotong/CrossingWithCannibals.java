package sotong;

/**
 * 
Our company is patron to some scholars who are researching the ecosystem of a jungle.
The scholars have help from the cannibals to conduct their study. However, they must use caution as the cannibals
will prey on the scholars when the number of the cannibals is larger than the number of scholars. 

One day, the scholars decided to explore the jungle by crossing the river together with the cannibals. Due to a group of crocodiles,
it is impossible to cross the river without a boat. However, fortunately there is a boat on their side of the river, but the boat is too
small to take all people in it at once. In other words, the number of people able to get into the boat is limited.
The following are the rules for allowing the boat to cross the river:

● Do not exceed the limited number of people.
● Once the cannibals or scholars get on the boat, they get off the boat first and then get in it again.
● The boat requires at least one cannibal or scholar to paddle. 

Under these rules, all of the cannibals and scholars must cross the river by the boat in several stages. At this time, you must
never forget that the cannibals should not outnumber the scholars. 

Write a program to calculate the minimum number of times you are required to move the boat across the river for all of the
cannibals and scholars to reach the other side. 

Time limit: 1 second (java: 2 seconds)

[Input] 
Several test cases can be included in the inputs. T, the number of cases is given in the first row of the inputs.
After that, the test cases as many as T (T ≤ 20) are given in a row. 
N, the numbers of the cannibals and scholars (i.e., N cannibals and N scholars) is given in the first row of each test case
and M, the number of people who can get on the boat is given separately as a blank. (1 ≤ N ≤ 10, 1 ≤ M ≤ 5)

[Output]
Output the minimum number of times for moving the boat across the river for all of the cannibals and scholars to cross in each case.
If there is no case of crossing the river without the scholars dying then output equals "impossible".

[I/O Example]

Input 
2
2 2
9 5

Output
5

11

=========== Sample input

5

3 2

4 3

5 2

7 5

9 5


 * @author naren
 *
 */
public class CrossingWithCannibals {
	static int readIndex = 0;
	static int insertIndex = 0;
	static int boatLimit;
	static State[] list = null;
	static int minTrips = Integer.MAX_VALUE;

	public static class State {
		int sCount;
		int cCount;
		boolean boatDirection; // false -> towards des, true -> towards source
		State parentState;
		int level;

		public State(int sC, int cC, boolean dir, State parent, int lev) {
			sCount = sC;
			cCount = cC;
			boatDirection = dir;
			parentState = parent;
			level = lev;
		}

		public boolean isValid() {
			if(this.sCount < this.cCount) {
				return false;
			}
			return true;
		}

		@Override
		public boolean equals(Object obj) {
			if(this == (State)obj) {
				return true;
			}
			State state = (State) obj;
			if(this.cCount == state.cCount && this.sCount == state.sCount && this.boatDirection == state.boatDirection) {
				return true;
			}
			return false;
		}
	}

	static void addToList(State state) {
		if(state.parentState == null) {
			list[insertIndex++] = state;
			return;
		}
		if(state.isValid()) {
			list[insertIndex++] = state;
		}
	}

	static void generateStates(State state) {
		for(int i = 0; i <= boatLimit; i++) {
			for(int j = 0; j <= boatLimit; j++) {
				if(i == 0 && j == 0) {
					continue;
				}
				if(i + j > boatLimit) {
					break;
				}
				State newState = new State(i, j, !state.boatDirection, state, state.level + 1);
				System.out.println(i + " " + j + " " + !state.boatDirection + " " + (state.level + 1));
				addToList(newState);
			}
		}
	}
	
	static void process(State start, State end) {
		addToList(start);
		while(list[readIndex] != null) {
			State state = list[readIndex++];
			if(state.equals(end)) {
				minTrips = Math.min(state.level, minTrips);
			}
			generateStates(state);
		}
	}
	
	public static void main(String[] args) {
		list = new State[1000];
		boatLimit = 2;
		process(new State(2, 2, false, null, 0), new State(2, 2, true, null, 0));
	}
}
