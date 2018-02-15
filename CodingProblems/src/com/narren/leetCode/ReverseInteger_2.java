package com.narren.leetCode;

public class ReverseInteger_2 {

    public int reverse(int x) {
        long res = 0;
        
        int t = x;
        
        while(t != 0) {
            res *= 10;
            res += (t % 10);
            t /= 10;
        }
        
        if(res > Integer.MAX_VALUE || res < Integer.MIN_VALUE) {
            return 0;
        }
        
        return (int)res;
    }
    
    public static void main(String[] args) {
		System.out.println(new ReverseInteger_2().reverse(-123));
	}
}
