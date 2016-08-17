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
	}
	
	static void addToList(State state, State[] list) {
		if(state.parentState == null) {
			list[insertIndex++] = state;
			return;
		}
		for() {
			
		}
		
	}
	
	static boolean isValid(State state) {
		if(state.sCount == 0 && state.cCount == 0) {
			return false;
		}
		if(state.sCount < state.cCount) {
			return false;
		}
		return true;
	}
}
