package com.narren.leetCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AutoComplete {

	class Trie {
		boolean isEnd;
		HashMap<Character, Trie> map;
	}
	
	Trie root;
	
	void insert(String word) {
		Trie node = root;
		for(char c : word.toCharArray()) {
			node.isEnd = false;
			Trie next = new Trie();
			node.map.put(c, next);
			node = next;
		}
		node.isEnd = true;
	}

	List<String> getSugesstions(String input, Trie root) {
		
		if(input == null || root == null) {
			return null;
		}
		
		char[] inputChars = input.toCharArray();
		
		for(char c : inputChars) {
			if(root.map.containsKey(c)) {
				root = root.map.get(c);
			}
		}		
	}
	
	StringBuilder traverse(ArrayList<String> list, Trie root, StringBuilder str) {
		if(root == null) {
			return str;
		}
		
		if(root.isEnd) {
			str.append(b);
		}
		
	}
}
