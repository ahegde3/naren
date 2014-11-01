import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


public class HiringNewEmployees {
	static int N;
	static int [] Count = new int[300005];
	static int [] Si = new int[32001];

	public static void main(String args[]) throws Exception	{
		InputStreamReader isr = new InputStreamReader(System.in);
		// isr = new InputStreamReader(new FileInputStream("input.txt"));
		BufferedReader br = new BufferedReader(isr);
		
		int T = Integer.parseInt(br.readLine());
		for(int tc = 0; tc < T; tc++) {
			for(int i=0; i<32001; i++) {
				Si[i] = 0;
			}
			
			N = Integer.parseInt(br.readLine());
			StringTokenizer st = new StringTokenizer(br.readLine());
			
			for(int i=1;i<=N;i++) {
				Count[i] = Integer.parseInt(st.nextToken());
				Si[Count[i]] ++;
			}
			
			int sum = 0;
			for(int i=32000;i>0;i--) {
				if(Si[i] > 0) { 
					int temp = Si[i]; 
					Si[i] = sum+1;
					sum += temp;
				}
			}
			
			StringBuffer sb = new StringBuffer();
			
			for(int i=1; i<=N; i++) {
				sb.append(Si[Count[i]] + " ");
			}
			
			System.out.println(sb.toString());
		}
	}
}
