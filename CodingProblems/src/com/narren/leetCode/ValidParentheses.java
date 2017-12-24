package com.narren.leetCode;

import java.util.Stack;

/**
 * Given a string containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.

The brackets must close in the correct order, "()" and "()[]{}" are all valid but "(]" and "([)]" are not.

 * 
 * @author naren
 *
 */
public class ValidParentheses {
	public boolean isValid(String s) {
		if(s.length() % 2 > 0)  {
			return false;
		}
		char[] chars = s.toCharArray();
		int m = chars.length / 2;

		Stack<Character> stack = new Stack<Character>();

		for(int i = 0; i < chars.length; i++) {
			if(chars[i] == '{' || chars[i] == '[' || chars[i] == '(') {
				stack.push(chars[i]);
			}

			if(chars[i] == '}' || chars[i] == ']' || chars[i] == ')') {
				if(stack.isEmpty()) {
					return false;
				}
				switch (chars[i]) {
				case '}':
					if(stack.pop() != '{') {
						return false;
					}	
					break;

				case ']':
					if(stack.pop() != '[') {
						return false;
					}
					break;
				case ')':
					if(stack.pop() != '(') {
						return false;
					}
				}
				
			}
		}

		return stack.isEmpty();
	}

	public static void main(String[] args) {
		ValidParentheses vp = new ValidParentheses();
		System.out.println(vp.isValid("(("));
	}
}
