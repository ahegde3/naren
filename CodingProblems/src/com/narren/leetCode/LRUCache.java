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

	void setHead(Node node) {
		if(head == null) {
			head = node;
			tail = node;
			return;
		}
		if(head == node) {
			return;
		}
		if(node.previous == null && node.next == null) {
			// New node
			node.next = head;
			head.previous = node;
			head = node;
			return;
		}

		if(node == tail) {
			// If its a tail
			node.previous.next = null;
			tail = node.previous;
			node.previous = null;
			node.next = head;
			head.previous = node;
			head = node;
			return;
		}
		if(node.previous != null)
			node.previous.next = node.next;
		if(node.next != null)
			node.next.previous = node.previous;
		node.previous = null;
		node.next = head;
		head.previous = node;
		head = node;
		return;

	}

	void removeTail() {
		if(tail == null) {
			return;
		}
		hashMap.remove(tail.key);
		if(head == tail) {
			head = null;
			tail = null;
			return;
		}
		
		tail.previous.next = null;
		Node temp = tail.previous;
		tail.previous = null;
		tail = temp;
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
		setHead(node);
		return node.value;
	}

	public void set(int key, int value) {
		Node node = hashMap.get(key);
		if(node != null) {
			node.value = value;
			setHead(node);
			return;
		}
		// No node found
		Node newNode = new Node(null, null, key, value);
		if(currentCapacity == CACHE_CAPACITY) {
			// Need to remove the tail and add this one to head
			removeTail();
			setHead(newNode);

		} else {
			setHead(newNode);
			currentCapacity++;
		}
		newNode.value = value;
		hashMap.put(key, newNode);
	}

	public static void main(String[] args) {
		LRUCache cache = new LRUCache(1);
		cache.set(2, 1);
		cache.set(2, 2);
		int i = cache.get(2);
		cache.set(1, 1);
		cache.set(4, 1);
		i = cache.get(2);
	}
}

class Node {
	Node previous;
	Node next;
	int value;
	int key;
	Node(Node p, Node n, int k, int v) {
		previous = p;
		next = n;
		key = k;
		value = v;
	}
}