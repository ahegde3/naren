package com.narren.hackerRank;


public class CandidateCode {
	public static void main(String[] args) {
		int max = PalindromeLengthPuzzle(new String[]{"Bha", "Bha",/* "Aka",*/ "Bha"/*, "Cha"*//*, "Bri", "Chi", "Brv", "Bha"*/});
		System.out.println(max);
	}
	 public static int PalindromeLengthPuzzle(String[] input1)
	    {
	        //Write code here
		 int maxLen = 1;
		 boolean bordersFound = true;
		 String start;
		 String end;
		 int eIndex = input1.length - 1;
		 int endIndex = input1.length - 1;
		 int nextIter = 0;
		 for (int j = 0; j < input1.length; j++) {
			 int tempMax = 0;
			 boolean founfPair = false;
			 if(bordersFound) {
				 endIndex = input1.length - 1 - j;
			 } else {
				 endIndex = eIndex - 1;
			 }
			 bordersFound = false;
			 for (int i = j; i <= endIndex; i++, endIndex--) {
				 founfPair = false;
				 start = input1[i].substring(0,1);
				 end = input1[endIndex].substring(0,1);
				 if (end.equals(start) && i != endIndex) {
					 founfPair = true;
					 if (endIndex == input1.length - 1 - j) {
						 // perfect borders
						 bordersFound = true;
						 eIndex = endIndex;
						 nextIter++;
					 }
					 tempMax += 2;
				 } else {
					 for (int k = endIndex - 1; k > i; k--) {
						 endIndex--;
						 end = input1[k].substring(0,1);
						 if (end.equals(start)) {
							 founfPair = true;
							 tempMax += 2;
							break; 
						 }
					 }
				 }
			 }
			 tempMax += founfPair ? 0 : 1;
			 tempMax += bordersFound ? 0 : nextIter*2;
			 maxLen = maxLen >= tempMax ? maxLen : tempMax;
		 }
		 return maxLen;
	    }
}
