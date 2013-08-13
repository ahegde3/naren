package com.narren.hackerMeter;

public class FibonacciSequence {

    private long[] sequence = new long[2];
    public static void main(String[] args) {
        new FibonacciSequence().compute(100);
    }
    private void compute(int n) {
        sequence = new long[n];
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
