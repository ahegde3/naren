package com.narren.leetCode;
/**
 * You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order and each of their nodes contain a single digit. Add the two numbers and return it as a linked list.

You may assume the two numbers do not contain any leading zero, except the number 0 itself.

Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
Output: 7 -> 0 -> 8


 * @author naren
 *
 */
public class AddTwoNumbers {
	public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		ListNode res = null;
		ListNode head = null;
		boolean carry = false;

		while(l1 != null || l2 != null) {
			int s = (l1 != null ? l1.val : 0) + (l2 != null ? l2.val : 0);
			if(carry) {
				s += 1;
				carry = false;
			}
			if(s > 9) {
				carry = true;
				s -= 10;
			}

			if(res == null) {
				res = new ListNode(s);
				head = res;
			} else {
				ListNode n = new ListNode(s);
				head.next = n;
				head = head.next;
			}
			l1 = l1 != null ? l1.next : null;
			l2 = l2 != null ? l2.next : null;
		}
		if(carry) {
			ListNode n = new ListNode(1);
			head.next = n;
			head = head.next;
		}
		return res;

	}
}
