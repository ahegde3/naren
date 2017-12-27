package com.narren.leetCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * Merge two sorted linked lists and return it as a new list. The new list should be made by splicing together the nodes of the first two lists.

Example:

Input: 1->2->4, 1->3->4
Output: 1->1->2->3->4->4

https://leetcode.com/problems/merge-two-sorted-lists/description/
 * @author naren
 *
 */
public class MergeTwoSortedLists {
	public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
		if(l1 == null) {
			return l2;
		}
		if(l2 == null) {
			return l1;
		}

		ListNode temp = new ListNode(-1);

		ListNode resNode = null;
		while(l1 != null && l2 != null) {
			if(l1.val < l2.val) {
				if(resNode == null) {
					resNode = l1;
					l1 = l1.next;
					temp.next = resNode;
					continue;
				}
				resNode.next = l1;
				resNode = resNode.next;
				l1 = l1.next;
			} else if(l2.val < l1.val) {
				if(resNode == null) {
					resNode = l2;
					l2 = l2.next;
					temp.next = resNode;
					continue;
				}
				resNode.next = l2;
				resNode = resNode.next;
				l2 = l2.next;
			} else {
				if(resNode == null) {
					resNode = l1;
					temp.next = resNode;
				} else {
					resNode.next = l1;
					resNode = resNode.next;
				}
				l1 = l1.next;
				resNode.next = l2;
				resNode = resNode.next;
				l2 = l2.next;
			}
		}

		if(l1 != null) {
			resNode.next = l1;

		}
		if(l2 != null) {
			resNode.next = l2;
		}

		return temp.next;
	}

	public static int[] stringToIntegerArray(String input) {
		input = input.trim();
		input = input.substring(1, input.length() - 1);
		if (input.length() == 0) {
			return new int[0];
		}

		String[] parts = input.split(",");
		int[] output = new int[parts.length];
		for(int index = 0; index < parts.length; index++) {
			String part = parts[index].trim();
			output[index] = Integer.parseInt(part);
		}
		return output;
	}

	public static ListNode stringToListNode(String input) {
		// Generate array from the input
		int[] nodeValues = stringToIntegerArray(input);

		// Now convert that list into linked list
		ListNode dummyRoot = new ListNode(0);
		ListNode ptr = dummyRoot;
		for(int item : nodeValues) {
			ptr.next = new ListNode(item);
			ptr = ptr.next;
		}
		return dummyRoot.next;
	}

	public static String listNodeToString(ListNode node) {
		if (node == null) {
			return "[]";
		}

		String result = "";
		while (node != null) {
			result += Integer.toString(node.val) + ", ";
			node = node.next;
		}
		return "[" + result.substring(0, result.length() - 2) + "]";
	}

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String line;
		while ((line = in.readLine()) != null) {
			ListNode l1 = stringToListNode(line);
			line = in.readLine();
			ListNode l2 = stringToListNode(line);

			ListNode ret = new MergeTwoSortedLists().mergeTwoLists(l1, l2);

			String out = listNodeToString(ret);

			System.out.print(out);
		}
	}
}
