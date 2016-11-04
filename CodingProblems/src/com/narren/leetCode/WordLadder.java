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
//		mySet1.add("hot");
//		mySet1.add("dog");
//		mySet1.add("dot");
//		mySet1.add("cog");
//		mySet1.add("pot");
		mySet1.add("hot");
		mySet1.add("cog");
		mySet1.add("dot");
		mySet1.add("dog");
		mySet1.add("hit");
		mySet1.add("lot");
		mySet1.add("log");

		System.out.println(wl.ladderLength("hit", "cog", mySet1));
	}
	
	
	class Node {
		String word;
		int steps;
		
		Node(String w, int s) {
			word = w;
			steps = s;
		}
	}
	
	Node[] queue;
	int enquequeIndex = -1;
	int dequeueIndex = -1;
	
	Node dequeue() {
		if(dequeueIndex < 0) {
			return null;
		}
		if(dequeueIndex == enquequeIndex) {
			Node n = queue[dequeueIndex];
			dequeueIndex = -1;
			enquequeIndex = -1;
			return n;
		}
		return queue[dequeueIndex++];
	}
	
	boolean isEmpty() {
		return (enquequeIndex == -1 && dequeueIndex == -1);
	}
	void enqueue(Node n) {
		if(isEmpty()) {
			enquequeIndex = 0;
			dequeueIndex = 0;
			queue[enquequeIndex] = n;
			return;
		}
		queue[++enquequeIndex] = n;
		
	}
	
	public int ladderLength(String beginWord, String endWord, Set<String> wordList) {
		String[] dic = new String[wordList.size() + 1];
		queue = new Node[wordList.size() + 1];
		int index = 0;
		for(String s : wordList) {
			dic[index++] = s;
		}
		dic[index] = endWord;
		if(beginWord.equals(endWord)) {
			return 0;
		}
		boolean[] visited = new boolean[dic.length];
		enqueue(new Node(beginWord, 1));
		while(!isEmpty()) {
			Node n = null;
			n = dequeue();
			if(n != null) {
				if(n.word.equals(endWord)) {
					return n.steps;
				}
				for(int i = 0; i < dic.length; i++) {
					if(!visited[i] && dic[i] != null && isOneLetterDifference(dic[i].toCharArray(), n.word.toCharArray())) {
						visited[i] = true;
						enqueue(new Node(dic[i], n.steps + 1));
						dic[i] = null;
					}
				}
			}
		}
		return 0;
	}
	
	boolean isOneLetterDifference(char[] a, char[] b) {
		boolean foundDiff = false;
		for(int i = 0 ; i < a.length; i++) {
			if(a[i] != b[i]) {
				if(foundDiff) {
					return false;
				}
				foundDiff = true;
			}
		}
		return true;
	}
}
