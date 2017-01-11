package com.narren.leetCode;

import java.util.HashMap;

public class LRUCache {
	final int CACHE_CAPACITY;
	int currentCapacity = 0;
	HashMap<Integer, Node> hashMap;
	Node head;
	Node tail;

	public LRUCache(int capacity) {
		CACHE_CAPACITY = capacity;
		hashMap = new HashMap<Integer, Node>(CACHE_CAPACITY);
	}

	public int get(int key) {
		Node node = hashMap.get(key);
		if(node == null) {
			return -1;
		}
		if(head == node) {
			// Already on top, no need to do anything
			return node.value;
		}
		if(tail == node) {
			// This the last node
			tail = node.previous;
			node.previous.next = null;
			node.next = head;
			node.previous = null;
			head.previous = node;
			head = node;
		} else {
			// Somewhere in between
			// number two node
			node.previous.next = node.next;
			if(node.next != null) {
				node.next.previous = node.previous;					
			}
			node.next = head;
			head.previous = node;
			head = node;

		}
		return node.value;
	}

	public void set(int key, int value) {

	}
}

class Node {
	Node previous;
	Node next;
	int value;
	Node(Node p, Node n, int v) {
		previous = p;
		next = n;
		value = v;
	}
}