package com.narren.sotong;

import java.util.Scanner;

/**
 * 
 * Refer to the screen shots with the same name
 * 
Sample Input

5
3 100
75 45 80
30 55 95
2 100
65 90
20 30
5 150
35 105 100 45 75
115 75 55 35 105
7 150
70 95 15 65 85 75 55
105 80 10 90 115 110 45
8 200
35 30 50 80 70 15 10 40
70 20 20 85 65 40 25 50

Expected output
15
-1
25
-1
45
 * @author naren
 *
 */
public class SamsungTire {

	static int K;
	static int N;
	static int[] inflate;
	static int[] deflate;
	static int res = Integer.MAX_VALUE;
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while(T > 0) {
			N = sc.nextInt();
			K = sc.nextInt();
			inflate = new int[N];
			deflate = new int[N];
			int[] num = new int[N];
			int[] result = new int[N];
			int[] count = new int[N];
			res = Integer.MAX_VALUE;
			for(int i = 0; i < N; i++) {
				inflate[i] = sc.nextInt();
				num[i] = i;
				count[i] = 1;
			}
			for(int i = 0; i < N; i++) {
				deflate[i] = sc.nextInt();
			}
			getPermutation(count, result, 0, num);
			System.out.println(res == Integer.MAX_VALUE ? "-1" : res);
			T--;
		}
	}
	
	static int[] getPermutation(int[] count, int[] result, int level, int[] num) {
		for(int i = 0; i < count.length; i++) {
			if(count[i] > 0) {
				result[level] = num[i];
				int[] resultArr = getPermutation(getNewArray(count, i), result, level + 1, num);
				if(resultArr == null) {
					return null;
				}
			}
		}
		boolean needPermutation = true;
		for(int j = 0; j < result.length; j++) {
			if(count[j] > 0) {
				needPermutation = false;
				break;
			}
		}
		if(needPermutation) {
			res = Math.min(process(result), res);
		}
		return result;
	}
	
	static int[] getNewArray(int[] in, int j) {
		int[] newArr = new int[in.length];
		for(int i = 0; i < in.length; i++) {
			if(i == j) {
				newArr[i] = in[i] - 1;
			} else
				newArr[i] = in[i];
		}
		return newArr;
	}
	
	static int process(int[] res) {
		for(int i = 0; i <= K; i++) {
			int initP = i;
			boolean completed = true;
			for(int j = 0 ; j < res.length; j++) {
				int infV = inflate[res[j]];
				int defV = deflate[res[j]];
				int afterInf = infV + initP;
				int afterDef = afterInf - defV;
				if(afterDef < 0 || afterInf > K) {
					completed = false;
					break;
				}
				initP = afterDef;
			}
			if(completed) {
				return i;
			}
		}
		return Integer.MAX_VALUE;
	}
}
