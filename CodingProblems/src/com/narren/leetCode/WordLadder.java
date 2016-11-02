package com.narren.leetCode;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * https://leetcode.com/problems/word-ladder/
 * 
Given two words (beginWord and endWord), and a dictionary's word list, find the length of shortest transformation sequence from beginWord to endWord, such that:

Only one letter can be changed at a time
Each intermediate word must exist in the word list
For example,

Given:
beginWord = "hit"
endWord = "cog"
wordList = ["hot","dot","dog","lot","log"]
As one shortest transformation is "hit" -> "hot" -> "dot" -> "dog" -> "cog",
return its length 5.

Note:
Return 0 if there is no such transformation sequence.
All words have the same length.
All words contain only lowercase alphabetic characters.
 * 
 * @author ns.bisht
 *
 */
public class WordLadder {


	public static void main(String[] args) {
		WordLadder wl = new WordLadder();
		Set mySet1 = new HashSet();
		mySet1.add("hot");
		mySet1.add("dog");
		mySet1.add("dot");

		System.out.println(wl.ladderLength("hot", "dog", mySet1));
	}
	public int ladderLength(String beginWord, String endWord, Set<String> wordList) {
		String[] dic = new String[wordList.size()];
		int index = 0;
		for(String s : wordList) {
			dic[index++] = s;
		}
		boolean[] visited = new boolean[wordList.size()];
		int min = process(beginWord, endWord, dic, visited, 0, Integer.MAX_VALUE);
		if(min == Integer.MAX_VALUE) {
			return 0;
		}
		return min;
	}

	int process(String currentStr, String endString, String[] dic, boolean[] visited, int steps, int min) {
		if(steps >= min) {
			return Integer.MAX_VALUE;
		}
		if(currentStr.equals(endString)) {
			min = Math.min(min, steps);
		}
		for(int i = 0; i < dic.length; i++) {
			if(!visited[i]) {
				visited[i] = true;
				if(!currentStr.equals(dic[i]) && isOneLetterDifference(currentStr.toCharArray(), dic[i].toCharArray())) {
					return process(dic[i], endString, dic, getVisited(visited), steps + 1, min);
				}
			}
		}
		return min;
	}

	boolean[] getVisited(boolean[] visited) {
		boolean[] newVisited = new boolean[visited.length];
		for(int i = 0; i < visited.length; i++) {
			newVisited[i] = visited[i];
		}
		return newVisited;
	}

	boolean isOneLetterDifference(char[] a, char[] b) {
		boolean foundDiff = false;
		for(int i = 0 ; i < a.length; i++) {
			if(a[i] != b[i] && !foundDiff) {
				foundDiff = true;
			} else {
				if(foundDiff) {
					return false;
				}
			}
		}
		return true;
	}
}
