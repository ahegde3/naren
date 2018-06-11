package com.narren.leetCode;

public class EditDistance_1 {

	public static void main(String[] args) {
		System.out.println(minDistance("zoologicoarchaeologist", "zoogeologist"));
	}
	public static int minDistance(String word1, String word2) {
		if(word1 == null && word2 == null) {
			return 0;
		}

		if(word1.length() == 0 || word2.length() == 0) {
			return Math.abs(word1.length() - word2.length());
		}

		if(word1.equals(word2)) {
			return 0;
		}

		int[][] distance = new int[word1.length() + 1][word2.length() + 1];


		// first row
		for(int i = 0; i <= word2.length(); i++) {
			distance[0][i] = i;
		}

		// First column
		for(int i = 0; i <= word1.length(); i++) {
			distance[i][0] = i;
		}

		for(int i = 1; i <= word1.length(); i++) {
			for (int j = 1; j <= word2.length(); j++) {
				int min = Math.min(Math.min(distance[i - 1][j], distance[i][j - 1]), distance[i - 1][j - 1]);

				if(word1.charAt(i - 1) == word2.charAt(j - 1)) {
					distance[i][j] = distance[i - 1][j - 1];
				} else {
					distance[i][j] = min + 1;
				}	

			}
		}

		return distance[word1.length()][word2.length()];
	}
}
