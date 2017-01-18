package com.narren.leetCode;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 
 * 
Given a string, sort it in decreasing order based on the frequency of characters.

Example 1:

Input:
"tree"

Output:
"eert"

Explanation:
'e' appears twice while 'r' and 't' both appear once.
So 'e' must appear before both 'r' and 't'. Therefore "eetr" is also a valid answer.
Example 2:

Input:
"cccaaa"

Output:
"cccaaa"

Explanation:
Both 'c' and 'a' appear three times, so "aaaccc" is also a valid answer.
Note that "cacaca" is incorrect, as the same characters must be together.
Example 3:

Input:
"Aabb"

Output:
"bbAa"

Explanation:
"bbaA" is also a valid answer, but "Aabb" is incorrect.
Note that 'A' and 'a' are treated as two different characters.

https://leetcode.com/problems/sort-characters-by-frequency/

 * 
 * @author naren
 *
 */
public class SortCharactersByFrequency {

	public static void main(String[] args) {
		SortCharactersByFrequency instance = new SortCharactersByFrequency();
		System.out.println(instance.frequencySort("cccaaab"));
	}
	public String frequencySort(String s) {
		HashMap<Character, Integer> map = new HashMap<Character, Integer>();
		int maxFreq = 1;
		for(char c : s.toCharArray()) {
			if(map.containsKey(c)) {
				int i = map.get(c);
				map.put(c, i + 1);
				maxFreq = Math.max(maxFreq, i + 1);
			} else {
				map.put(c, 1);
			}
		}

		StringBuilder[] charCount = new StringBuilder[maxFreq + 1];
		for(Entry<Character, Integer> entry : map.entrySet()) {
			if(charCount[entry.getValue()] != null) {
				int i = entry.getValue();
				while(i > 0) {
					charCount[entry.getValue()].append(entry.getKey());
					i--;
				}
			} else {
				charCount[entry.getValue()] = new StringBuilder();
				int i = entry.getValue();
				while(i > 0) {
					charCount[entry.getValue()].append(entry.getKey());
					i--;
				}
			}
		}

		StringBuilder res = new StringBuilder();
		for(int i = maxFreq; i > 0; i--) {
			if(charCount[i] != null) {
				res.append(charCount[i]);
			}
		}
		return res.toString();
	}
}
