import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


public class Sum {
	static int N = 100;
	static int[] input = new int[100];
	static int Answer1, Answer2;

	public static void main(String[] args) throws Exception {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		StringTokenizer st = new StringTokenizer(br.readLine());
		for (int i = 0;i < 100 ; i++) {
			int number = Integer.parseInt(st.nextToken());
			if (number > 0) {
				Answer2 += number;
			} else {
				Answer1 += number;
			}
		}
		System.out.print(Answer1 + " " + Answer2);
	}
	
}
