package com.narren.coding.interview;

public class PermutationOfStringSelf {

	char[] result;
	void process(String s, int[] count, int level) {
		for(int i= 0; i < count.length; i++){
			if(count[i] > 0) {
				result[level] = (char) i;
				count[i] = count[i] - 1;
				process(s, count, level + 1);
				count[i] = count[i] + 1;
			}
		}
		for(int i = 0; i < 123; i++) {
			if(count[i] != 0) {
				break;
			}
			if(i == 122) {
				for (char c : result) {
					System.out.print(c);	
				}	
				System.out.println();
			}
		}

		

	}

	public static void main(String[] args) {
		PermutationOfStringSelf self = new PermutationOfStringSelf();
		
		String s = "baca";
		self.result = new char[s.length()];
		int[] count = new int[123];
		for(int i= 0; i < s.length(); i++){
			count[s.charAt(i)]++;
		}
		self.process(s, count, 0);
	}
}
