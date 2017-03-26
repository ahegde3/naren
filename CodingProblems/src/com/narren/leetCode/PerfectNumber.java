package com.narren.leetCode;import com.oracle.webservices.internal.api.EnvelopeStyle.Style;

/**
 * 
We define the Perfect Number is a positive integer that is equal to the sum of all its positive divisors except itself.

Now, given an integer n, write a function that returns true when it is a perfect number and false when it is not.
Example:
Input: 28
Output: True
Explanation: 28 = 1 + 2 + 4 + 7 + 14
Note: The input number n will not exceed 100,000,000. (1e8)
 * 
 * @author naren
 *
 */
public class PerfectNumber {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		System.out.println(new PerfectNumber().checkPerfectNumber(28));
		System.out.println(System.currentTimeMillis() - start);
	}
	public boolean checkPerfectNumber(int num) {
		if(num < 3) {
            return false;
        }
		int sum = 1;
		int limit = (int)Math.sqrt(num);
		for(int i = 2; i <= limit; i++) {
			if(num % i == 0) {
				sum += i;
				int t = (num / i);
				if(t != i) {
					sum += t;					
				}

			}
			if(sum > num) {
				return false;
			}
		}
		return sum == num;
	}
}
