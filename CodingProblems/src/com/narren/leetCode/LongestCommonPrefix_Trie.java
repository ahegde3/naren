package com.narren.leetCode;

import java.util.HashMap;
import java.util.Map;

public class LongestCommonPrefix_Trie {

	public String longestCommonPrefix(String[] strs) {
		TrieNode root = formTrieTree(strs);
		String lcp = "";
		if(root == null) {
			return lcp;
		}
		TrieNode node = root.charMap.get('!');
		while(node != null) {
			if(node.charMap == null || node.charMap.size() > 1) {
				return lcp;
			}
			Character key = null;
			for(Map.Entry<Character, TrieNode> entry : node.charMap.entrySet()) {
				key = entry.getKey();
			}
			lcp += key;
			node = node.charMap.get(key);
		}
		return lcp;
	}
	
	TrieNode formTrieTree(String[] strs) {
		Map<Character, TrieNode> rootMap = new HashMap<>();
		TrieNode mainNode = new TrieNode(null, true);
		rootMap.put('!', mainNode);
		
		TrieNode rootNode = new TrieNode(rootMap, false);
		
		
		for(String str : strs) {
			TrieNode currNode = rootNode.charMap.get('!');
			char[] chars = str.toCharArray();
			if(chars.length < 1) {
				return null;
			}
			for(char c : chars) {
				currNode.isEnd = false;
				Map<Character, TrieNode> map = currNode.charMap;
				if(map == null) {
					map = new HashMap<>();
				}
				if(!map.containsKey(c)) {
					map.put(c, new TrieNode(null, true));	
				}
				
				currNode.charMap = map;
				currNode = currNode.charMap.get(c);
				
			}
		}
		return rootNode;
				
	}
	
	public static void main(String[] args) {
		String[] strs = new String[]{"aa", "a"};
		System.out.println(new LongestCommonPrefix_Trie().longestCommonPrefix(strs));
	}

}

class TrieNode {
	Map<Character, TrieNode> charMap;
	boolean isEnd;
	
	public TrieNode(Map<Character, TrieNode> cMap, boolean end) {
		this.charMap = cMap;
		this.isEnd = end;
	}
}
