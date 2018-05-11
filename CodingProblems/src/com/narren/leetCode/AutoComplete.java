package com.narren.leetCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class AutoComplete {

	class Trie {
		boolean isEnd;
		HashMap<Character, Trie> map;
	}

	public static void main(String[] args) {
		AutoComplete ac = new AutoComplete();
		Trie root = ac.new Trie();
		ac.insert("Alice", root);
		ac.insert("Alison", root);
		ac.insert("Bob", root);
		ac.insert("Boby", root);
		ac.insert("Narendra", root);

		System.out.println(ac.getSugesstions("Na", root));
		System.out.println(ac.getSugesstions("Al", root));
		
		System.out.println(ac.isPresent("Bob", root));
		System.out.println(ac.isPresent("Narend", root));
	}

	void insert(String word, Trie root) {
		Trie node = root;
		for(char c : word.toCharArray()) {
			if(node.map == null) {
				node.map = new HashMap<>();
			}
			if(!node.map.containsKey(c)) {
				Trie next = new Trie();
				node.map.put(c, next);	
				node = next;
			} else {
				node = node.map.get(c);
			}


		}
		node.isEnd = true;
	}

<<<<<<< HEAD
=======
	boolean isPresent(String word, Trie root) {
		if(root == null) {
			return false;
		}
		
		for(char c : word.toCharArray()) {
			if(root != null && root.map != null && root.map.containsKey(c)) {
				root = root.map.get(c);
			} else {
				return false;
			}
		}
		
		return root.isEnd;
	}


>>>>>>> 874f0991ae9de0108abf3937c16ef78c8f628fe1
	List<String> getSugesstions(String input, Trie root) {

		if(input == null || root == null || root.map == null) {
			return null;
		}

		for(char c : input.toCharArray()) {
			if(root.map.containsKey(c)) {
				root = root.map.get(c);
			}
		}
		ArrayList<String> list = new ArrayList<>();

		traverse(list, root, "");

		return list;
	}
	//	
	void traverse(ArrayList<String> list, Trie root, String s) {
		if(root == null || root.map == null) {
			return;
		}


		for(Entry<Character, Trie> entry : root.map.entrySet()) {
			if(entry.getValue().isEnd) {
				list.add(s + entry.getKey());
			}
			traverse(list, entry.getValue(), s + entry.getKey().toString());
		}

	}
}
