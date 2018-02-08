package com.narren.leetCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PalindromeLinkedList {

	public boolean isPalindrome(ListNode head) {
		ListNode slow = head;
		ListNode fast = head;
		boolean evenLen = true;
		
		while(slow != null && fast != null) {
			slow = slow.next;
			if(fast.next != null) {
				fast = fast.next.next;
			} else {
				fast = fast.next;
				evenLen = false;
			}
			
		}
		
		ListNode curr = slow;
		ListNode prev = null;
		
		while (curr != null) {
			ListNode temp = curr.next;
			curr.next = prev;
			prev = curr;
			curr = temp;
		}
		
		while (head != null && prev != null) {
			if(head.val != prev.val) {
				return false;
			}
			head = head.next;
			prev = prev.next;
		}
		
		return true;
		
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

	public static String booleanToString(boolean input) {
		return input ? "True" : "False";
	}

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String line;
		while ((line = in.readLine()) != null) {
			ListNode head = stringToListNode(line);

			boolean ret = new PalindromeLinkedList().isPalindrome(head);

			String out = booleanToString(ret);

			System.out.print(out);
		}
	}
}
