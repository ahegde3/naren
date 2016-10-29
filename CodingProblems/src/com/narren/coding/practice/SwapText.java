package com.narren.coding.practice;

/**
 * Order the sentences in a paragraph based on sentence length.
 * @author naren
 *
 */
public class SwapText {

	static String process(String input) {
		char[] inputArr = input.toCharArray();
		int numSentences = 0;
		for(char i : inputArr) {
			if(i == '?' || i == '!' || i == '.') {
				numSentences++;
			}
		}
		int[] sentenceLenArr = new int[numSentences];
		int[] sentenceStartIndex = new int[numSentences];
		int insertIndex = 0;
		int senLen = 0;
		int maxLen = 0;
		
		for(int i = 0; i < inputArr.length; i++) {
			if(i == '?' || i == '!' || i == '.') {
				if(i != 0) {
					sentenceStartIndex[++insertIndex] = i + 2;
				} else {
					sentenceStartIndex[insertIndex] = 0;
				}
				sentenceLenArr[insertIndex] = senLen;
				
				maxLen = Math.max(maxLen, senLen);
				senLen = -1;
			} else {
				senLen++;
			}
		}
		
		int[] finalArr = new int[maxLen + 1];
		
		//initialize the arr
		for(int i = 0 ; i < finalArr.length; i++) {
			finalArr[i] = -1;
		}
		
		for(int i = 0 ; i < sentenceLenArr.length; i++) {
			finalArr[sentenceLenArr[i]] = sentenceStartIndex[i];
		}
		
		char[] sortedString = new char[inputArr.length];
		int sortedIndex = 0;
		for(int i = 0 ; i < finalArr.length; i++) {
			if(finalArr[i] != -1) {
				int temp = finalArr[i];
				while(inputArr[temp] != '?' || inputArr[temp] != '!' || inputArr[temp] != '.') {
					sortedString[sortedIndex++] = inputArr[temp];
					temp++;
				}
				sortedString[sortedIndex++] = inputArr[temp];
				sortedString[sortedIndex++] = ' ';
			}
		}
		
		return new String(sortedString);
	}
}
