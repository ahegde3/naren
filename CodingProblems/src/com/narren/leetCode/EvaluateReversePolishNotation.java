package com.narren.leetCode;

import java.util.Stack;

/**
 * 
https://leetcode.com/problems/evaluate-reverse-polish-notation/

Evaluate the value of an arithmetic expression in Reverse Polish Notation.

Valid operators are +, -, *, /. Each operand may be an integer or another expression.

Some examples:
  ["2", "1", "+", "3", "*"] -> ((2 + 1) * 3) -> 9
  ["4", "13", "5", "/", "+"] -> (4 + (13 / 5)) -> 6

 * @author ns.bisht
 *
 */
public class EvaluateReversePolishNotation {

	
	public static void main(String[] args) {
		
		System.out.println(eval(new String[]{"2", "1", "+", "3", "*"}));
	}
	
	static int eval(String[] in) {
		Stack<String> stack = new Stack<String>();
		int result;
		for(String s : in) {
			switch (s) {
			case "+": {
				int n1 = Integer.parseInt(stack.pop());
				int n2 = Integer.parseInt(stack.pop());
				result = n1 + n2;
				stack.push("" + result);
			}
				break;
			case "-": {
				int n1 = Integer.parseInt(stack.pop());
				int n2 = Integer.parseInt(stack.pop());
				result = n2 - n1;
				stack.push("" + result);
			}
				break;
			case "*": {
				int n1 = Integer.parseInt(stack.pop());
				int n2 = Integer.parseInt(stack.pop());
				result = n2 * n1;
				stack.push("" + result);
			}
			break;
			case "/":{
				int n1 = Integer.parseInt(stack.pop());
				int n2 = Integer.parseInt(stack.pop());
				result = n2 / n1;
				stack.push("" + result);
			}
				break;

			default:
				stack.push(s);
				continue;
			}
		}
		return Integer.parseInt(stack.pop());
	}
}
