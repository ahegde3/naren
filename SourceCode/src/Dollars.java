import java.util.Scanner;


public class Dollars {
		private static final int MOD = 100000000;
		private static long[][] data = new long[1001][101];
		
		public static void main(String args[]) throws Exception	{
			Scanner sc = new Scanner(System.in);
//			sc = new Scanner(new FileInputStream("input.txt"));

			int T = sc.nextInt();
			for(int tc = 0; tc < T; tc++) {
				int N = sc.nextInt();
				int K = sc.nextInt();
				
				long X = g(N, K);
				
				System.out.println(X % MOD);
			}
		}

		private static long g(int n, int k) {
			if (n == 0) return 1;
			if (k == 1) return 1;
			if (data[n][k] != 0) return data[n][k];
			
			long res = 0;
			for (int i = n; i >= 0; i -= k) {
				res += g(i, k-1) % MOD;
			}
			data[n][k] = res % MOD;
//			System.out.println("(" + n + ", " + k + ") " + res);
			return res;
		}


}
