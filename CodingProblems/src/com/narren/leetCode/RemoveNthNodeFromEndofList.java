package com.narren.leetCode;
/**
 * Given a linked list, remove the nth node from the end of list and return its head.

For example,

   Given linked list: 1->2->3->4->5, and n = 2.

   After removing the second node from the end, the linked list becomes 1->2->3->5.
Note:
Given n will always be valid.
Try to do this in one pass.

https://leetcode.com/problems/remove-nth-node-from-end-of-list/?tab=Description
 * @author naren
 *
 */
public class RemoveNthNodeFromEndofList {

	public ListNode removeNthFromEnd(ListNode head, int n) {
		if(head == null) {
			return null;
		}
		ListNode pointer1 = head, pointer2 = head;
		
		while(n > 0) {
			if(pointer1 != null) {
				pointer1 = pointer1.next;
			}
			n--;
		}
		
		while(pointer1 != null) {
			if(pointer1.next == null) {
				break;
			}
			pointer1 = pointer1.next;
			pointer2 = pointer2.next;
		}
		if(pointer2 == head) {
			head = head.next;
		} else {
			pointer2.next = pointer2.next.next;
		}
		return head;
		
	}
}
