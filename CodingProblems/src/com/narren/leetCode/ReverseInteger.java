package com.narren.leetCode;

public class ReverseInteger {
	public int reverse(int x) {
			if(x == Integer.MAX_VALUE || x == Integer.MIN_VALUE) {
				return 0;
			}
	        long[] r = new long[]{1, 0};
	        int t = Math.abs(x);
	        process(t, r);
	        if(r[1] < Integer.MIN_VALUE || r[1] > Integer.MAX_VALUE) {
	            return 0;
	        }
	        return x < 0 ? (int)-r[1] : (int)r[1];
    }
    
    void process(int x, long[] r) {
        if(x < 10) {
            r[1] += x * r[0];
            r[0] *= 10;
            return;
        }
        process(x / 10, r);
        r[1] += x % 10 * r[0];
        r[0] *= 10;
    }
    
    public static void main(String[] args) {
		ReverseInteger ri = new ReverseInteger();
		System.out.println(ri.reverse(-2147483648));
	}
}
