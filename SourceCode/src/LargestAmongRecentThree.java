import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


public class LargestAmongRecentThree {

	static int[] A = new int [100];
	static int Answer = 0;
	public static void main(String[] args) throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		StringTokenizer st = new StringTokenizer(br.readLine());
		for (int i = 0; i < 100 ; i++) {
			A[i] = Integer.parseInt(st.nextToken());
			if (i > 1) {
				int lastNumber = A[i-1];
				int secondLastNumber = A[i-2];
				if (A[i] > lastNumber && A[i] > secondLastNumber) {
					Answer = A[i];
					break;
				}
			}
			
		}
		System.out.println("" + Answer);
	}
}
