import java.util.Scanner;


public class SNS {

	static int[][]input;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T;
		T=sc.nextInt();
		for(int test_case = 1; test_case <= T; test_case++)
		{		
			int N = sc.nextInt();
			int dc = 0;
			int cc = 0;
			int ccElement = 0;
			input = new int[N][N];
			for (int i = 0; i < input.length; i++){
				int ones = 0;
				for (int j = 0; j < input[i].length; j++) {
					int element = sc.nextInt();
					input[i][j] = element;
					if (input[i][j] == 1) {
						ones++;
					}
		
			}
				if (ones >= dc) {
					dc = ones;
					ccElement = i;
				}
				ones = 0;
			}
			cc = findCc(ccElement);
			// Print the answer to standard output(screen).
			System.out.println("#" + test_case + " " + dc + " " + cc);
		}
	}
	
	private static int findCc(int dcIndex) {
		int finalCc = 0;
		for (int i = 0; i < input[dcIndex].length; i ++) {
			if (input[dcIndex][i] == 1) {
				finalCc++;
			} else if (dcIndex == i) {
				continue; // no self-loop
			} else if (input[dcIndex][i] == 0) {
				finalCc++;
				finalCc += findSteps(dcIndex, i);
				
			}
		}
		return finalCc;
	}
	
	private static int findSteps(int dcIndex, int missing) {
		int steps = 0;
		for (int i = 0;  i < input[dcIndex].length; i++) {
			if (dcIndex == i) {
				continue;	
			}
			if (input[dcIndex][i] == 1) {
				if (input[i][missing] == 1) {
					steps++;
					break;

				}
			}


		}
		return steps;
	}
}
