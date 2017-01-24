package com.narren.leetCode;

import java.util.Comparator;
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
	
	public static void main(String[] args) {
		ListNode lNode3 = new ListNode(5);
		
		ListNode lNode2 = new ListNode(3);
		lNode2.next = lNode3;
		lNode3.next = null;
		ListNode lNode = new ListNode(1);
		lNode.next = lNode2;
		
		
		ListNode node = new MergekSortedLists().mergeKLists(new ListNode[]{lNode});
		System.out.println(node);
	}
	public ListNode mergeKLists(ListNode[] lists) {
		if(lists == null) {
			return null;
		}
		if(lists.length == 1) {
			return lists[0];
		}
		PriorityQueue<ListNode> queue = new PriorityQueue<ListNode>(new Comparator<ListNode>() {

			@Override
			public int compare(ListNode o1, ListNode o2) {
				return o1.val - o2.val;
			}
		});
		for(ListNode ln : lists) {
			if(ln != null) {
				queue.offer(ln);
			}
		}
		
		ListNode head = null;
		ListNode tail = null;
		while(!queue.isEmpty()) {
			if(head == null) {
				head = queue.poll();
				tail = head;
			} else {
				ListNode n = queue.poll();
				tail.next = n;
				tail = n;
			}
			if(tail != null && tail.next != null) {
				queue.offer(tail.next);
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
