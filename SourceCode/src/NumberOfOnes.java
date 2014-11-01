import java.util.Scanner;


public class NumberOfOnes {
	static int N;
	static int Answer;

	public static void main(String args[]) throws Exception
	{

		Scanner sc = new Scanner(System.in);

		for(int test_case = 1; test_case <= 10; test_case++)
		{
			N = sc.nextInt();
			Answer = solution(N);

			System.out.println("#" + test_case + " " + Answer);
		}
	}
	private static int solution(int input) {
		int output = 0;
		String binaryStr = "";
		for (int i = 1 ; i <= input; i++) {
			binaryStr += Integer.toBinaryString(i);
			if (binaryStr.length() >= input) {
				break;
			}
		}

		for(int j = 0; j < input; j++) {
			char charAtIndex = binaryStr.charAt(j);
			if (charAtIndex == '1') {
				output++;
			}
		}
		return output;
	}


}
