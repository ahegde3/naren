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
				if(!node.isEnd) {
					key = entry.getKey();	
				} else {
					return lcp;
				}
				
			}
			lcp += key;
			node = node.charMap.get(key);
		}
		return lcp;
	}
	
	TrieNode formTrieTree(String[] strs) {
		Map<Character, TrieNode> rootMap = new HashMap<>();
		TrieNode mainNode = new TrieNode(null, false);
		rootMap.put('!', mainNode);
		
		TrieNode rootNode = new TrieNode(rootMap, false);
		
		
		for(String str : strs) {
			TrieNode currNode = rootNode.charMap.get('!');
			char[] chars = str.toCharArray();
			if(chars.length < 1) {
				return null;
			}
			for(int i = 0; i < chars.length; i++) {
				Map<Character, TrieNode> map = currNode.charMap;
				if(map == null) {
					map = new HashMap<>();
				}
				if(!map.containsKey(chars[i])) {
					TrieNode t = null;
					if(i == chars.length - 1 ) {
						t = new TrieNode(null, true);
					} else {
						t = new TrieNode(null, false);
					}
					map.put(chars[i], t);	
				}
				
				if(i == chars.length - 1 && !map.get(chars[i]).isEnd) {
					map.get(chars[i]).isEnd = true;
				}
				
				currNode.charMap = map;
				currNode = currNode.charMap.get(chars[i]);
				
			}
		}
		return rootNode;
				
	}
	
	public static void main(String[] args) {
		String[] strs = new String[]{"a", "aaaaaa"};
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
