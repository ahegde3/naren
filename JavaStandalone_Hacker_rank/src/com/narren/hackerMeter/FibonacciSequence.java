package com.narren.hackerMeter;

import java.util.Scanner;

public class FibonacciSequence {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int cases = Integer.parseInt(scanner.nextLine());
        for(int i = 0; i < cases; i++) {
          run(scanner);
        }
    }
    public static void run(Scanner scanner) {
        int sequence = scanner.nextInt();
        generateFibonacciSequence(sequence);
      }

    private static void generateFibonacciSequence(int n) {
        long[] sequence = new long[2];
        for (int i = 0; i < n; i++) {
            if(i == 0) {
                sequence[0] = 0;
                sequence[1] = 0;
            } else if (i == 1) {
                sequence[i] = 1;
            } else {
                long sum = sequence[0] + sequence[1];
                sequence[0] = sequence[1];
                sequence[1] = sum;
            }
            System.out.println(sequence[1]);
        }
    }
}