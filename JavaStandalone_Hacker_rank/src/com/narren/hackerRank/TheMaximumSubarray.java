package com.narren.hackerRank;

import java.util.Scanner;

public class TheMaximumSubarray {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        while (T > 0) {
            int N = sc.nextInt();
            int arr[] = new int[N];
            for(int i = 0; i < N ;i++) {
                arr[i] = sc.nextInt();
            }
            process(arr);
            T--;
        }
    }
    
    private static void process(int[] arr) {
        int sum = arr[0];
        int max = Integer.MIN_VALUE;
        int positiveSum = arr[0];
        int pF = arr[0];
        for(int i = 1; i < arr.length; i++) {
            sum = Math.max(sum + arr[i], arr[i]);
            max = Math.max(max, sum);
            //positiveSum = Math.max(arr[i], positiveSum);
           positiveSum = Math.max(positiveSum + arr[i], arr[i]);
           pF = Math.max(pF + positiveSum, positiveSum);
        }
        System.out.println(Math.max(max, sum) + " " + positiveSum);
    }
}
