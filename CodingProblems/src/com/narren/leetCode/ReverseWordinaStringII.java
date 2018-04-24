package com.narren.leetCode;
/**
 * 
 *  Given an input string, reverse the string word by word. A word is defined as a sequence of non-space characters.

The input string does not contain leading or trailing spaces and the words are always separated by a single space.

For example,
Given s = "the sky is blue",
return "blue is sky the".

Could you do it in-place without allocating extra space?

Related problem: Rotate Array

Update (2017-10-16):
We have updated the function signature to accept a character array, so please reset to the default code
definition by clicking on the reload button above the code editor. Also, Run Code is now available! 
 * @author naren
 *
 */
public class ReverseWordinaStringII {
	
	public String reverseWords(char[] str) {
		
		int i = 0;
		int j = str.length - 1;
		
		reverse(str, i, j);
		int l = 0;
		for(int k = 0; k < str.length; k++) {
			if(str[k] == ' ') {
				reverse(str, l, k - 1);
				l = k + 1;
			}
		}
		
		reverse(str, l, str.length - 1);

		return new String(str);
	}

	void reverse(char[] str, int i, int j) {
		while(i < j) {
			char c = str[i];
			str[i] = str[j];
			str[j] = c;
			i++;
			j--;
		}
	}
	public static void main(String[] args) {
		ReverseWordinaStringII rws = new ReverseWordinaStringII();
		String s = "the sky is blue";
		System.out.println(rws.reverseWords(s.toCharArray()));
	}
}
