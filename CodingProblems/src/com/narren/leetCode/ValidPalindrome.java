package com.narren.leetCode;

/**
 * Given a string, determine if it is a palindrome, considering only alphanumeric characters and ignoring cases.

For example,
"A man, a plan, a canal: Panama" is a palindrome.
"race a car" is not a palindrome.

Note:
Have you consider that the string might be empty? This is a good question to ask during an interview.

For the purpose of this problem, we define empty string as valid palindrome.


 * @author naren
 *
 */
public class ValidPalindrome {
	public boolean isPalindrome(String s) {
		if(s == null || s.length() < 1) {
			return true;
		}

		char[] input = s.toCharArray();

		for(int i = 0, j = input.length - 1; i < j; i++, j--) {
			char c1 = '\0';
			char c2 = '\0';

			while(i < j) {
				if('0' <= input[i] && input[i] <= '9') {
					c1 = input[i];
					break;
				} else if('A' <= input[i] && input[i] <= 'Z') {
					c1 = (char)(input[i] - 'A');
					break;
				} else if('a' <= input[i] && input[i] <= 'z') {
					c1 = (char)(input[i] - 'a');
					break;
				} else {
					i++;
				}
			}

			while(i < j) {
				if('0' <= input[j] && input[j] <= '9') {
					c2 = input[j];
					break;
				} else if('A' <= input[j] && input[j] <= 'Z') {
					c2 = (char)(input[j] - 'A');
					break;
				} else if('a' <= input[j] && input[j] <= 'z') {
					c2 = (char)(input[j] - 'a');
					break;
				} else {
					j--;
				}
			}

			if(c1 != c2 && i < j) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		ValidPalindrome vp = new ValidPalindrome();
		System.out.println(vp.isPalindrome("race a car"));
		
	}
}
