package sotong;

import java.util.Scanner;

/**
 * 
 * Refer BeaconAdvertisement_X.jpg for problem description
 * 
Sample input
5
7 1 2 3 1 2 3
2 2
6 4
3 3
7 2
1 1
2 1
1 10
4 3 2 1 6 4 3
1 5
1 3
2 4
2 2
3 1 2 3 7 6 4
10 1
11 2
13 3
5 2 2 1 2 5 4
2 4
4 6
9 1
14 10
30 15
50 31 4 1 734 134 546
1 39
4 28
9 24
12 16
7 29
6 27
12 14
24 18
1 34
11 20
5 23
31 16
12 38
3 35
10 2
35 14
11 34
31 13
6 14
10 7
4 17
15 19
7 36
8 19
13 20
7 18
27 6
9 5
13 14
2 20
9 12
5 13
34 12
5 20
17 7
18 31
33 6
14 8
4 6
10 38
23 10
36 13
17 15
27 20
11 21
27 1
31 13
20 16
47 2
19 26

Expected output
12
18
17
16
17998


 * @author naren
 *
 */
public class BeaconAdvertisement {

	static int[][] cus;
	static int[][] add = new int[4][3];
	static int N;
	static int maxWidth = 0;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while(T > 0) {
			N = sc.nextInt();
			int L1 = sc.nextInt();
			int L2 = sc.nextInt();
			int L3 = sc.nextInt();
			int P1 = sc.nextInt();
			int P2 = sc.nextInt();
			int P3 = sc.nextInt();
//			add[1][1] = L1;
//			add[1][2] = P1;
//			add[2][1] = L2;
//			add[2][2] = P2;
//			add[3][1] = L1;
//			add[3][2] = P2;
			if(P1 > P2) {
				if(P1 > P3) {
					add[3][1] = L1;
					add[3][2] = P1;
					if(P3 > P2) {
						add[2][1] = L3;
						add[2][2] = P3;
						add[1][1] = L2;
						add[1][2] = P2;
					} else {
						add[2][1] = L2;
						add[2][2] = P2;
						add[1][1] = L3;
						add[1][2] = P3;
					}
				} else {
					add[3][1] = L3;
					add[3][2] = P3;
					add[2][1] = L1;
					add[2][2] = P1;
					add[1][1] = L2;
					add[1][2] = P2;
				}
			} else if(P2 > P1) {
				if(P2 > P3) {
					add[3][1] = L2;
					add[3][2] = P2;
					if(P3 > P1) {
						add[2][1] = L3;
						add[2][2] = P3;
						add[1][1] = L1;
						add[1][2] = P1;
					} else {
						add[2][1] = L1;
						add[2][2] = P1;
						add[1][1] = L3;
						add[1][2] = P3;
					}
				} else {
					add[3][1] = L3;
					add[3][2] = P3;
					add[2][1] = L2;
					add[2][2] = P2;
					add[1][1] = L1;
					add[1][2] = P1;
				}
			}
			cus = new int[3][N + 1];
			for(int i = 1; i <= N; i++) {
				for(int j = 1; j < 3; j++) {
					cus[j][i] = sc.nextInt();
					maxWidth = Math.max(maxWidth, cus[1][i] + cus[2][i]);
				}
			}
			process();
			T--;
		}
	}

	static int process() {
		int sum = 0;
		int maxSum = 0;
		int[][] resArr = new int[N  + 1][maxWidth + 1];
		for(int i = 1; i <= maxWidth - add[1][1]; i++) {
			for(int j = i + add[1][1]; j <= maxWidth - add[2][1]; j ++) {
				for(int k = j + add[2][1]; k <= maxWidth - add[3][1]; k++) {
					for(int l = 1; l <= N; l++) {
						if(cus[1][l] <= k && cus[1][l] + cus[2][l] >= k + add[3][1]) {
							sum += add[3][2];
						} else if(cus[1][l] <= j && cus[1][l] + cus[2][l] >= j + add[2][1]) {
							sum += add[2][2];
						} else if(cus[1][l] <= i && cus[1][l] + cus[2][l] >= i + add[1][1]) {
							sum += add[1][2];
						}
					}
					maxSum = Math.max(maxSum, sum);
					sum = 0;
				}
			}
		}
		return maxSum;
	}
}
