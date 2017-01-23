package com.narren.leetCode;

import java.util.PriorityQueue;

/**
 * 
Merge k sorted linked lists and return it as one sorted list. Analyze and describe its complexity.

https://leetcode.com/problems/merge-k-sorted-lists/
 * 
 * @author nsbisht
 *
 */
public class MergekSortedLists {
	public ListNode mergeKLists(ListNode[] lists) {
		int listLen = lists.length;
		int nullCount = 0;
		PriorityQueue<Integer> queue = new PriorityQueue<Integer>((a, b) -> (b - a));
		while(nullCount < listLen) {
			nullCount = 0;
			for(ListNode ln : lists) {
				if(ln == null) {
					nullCount++;
				} else {
					queue.offer(ln.val);
					ln = ln.next;
				}
			}
		}
		ListNode head = null;
		while(!queue.isEmpty()) {
			int val = queue.poll();
			if(head == null) {
				head = new ListNode(val);
				head.next = null;
			} else {
				ListNode node = new ListNode(val);
				node.next = head;
				head = node;
			}
		}
		return head;
	}
}

class ListNode {
	int val;
	ListNode next;
	ListNode(int x) { val = x; }
}
