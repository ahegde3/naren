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
			if(node.previous != null) {
				node.previous.next = node.next;				
			}
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
		Node node = hashMap.get(key);
		if(node != null) {
			node.value = value;
			return;
		}
		// No node found
		Node newNode = new Node(null, null, value);
		if(currentCapacity == CACHE_CAPACITY) {
			// Need to remove and add this one
			newNode.previous = tail.previous;
			if(tail.previous != null) {
				tail.previous.next = newNode;				
			}
			if(head == tail) {
				head = null;
				head = newNode;
				tail = newNode;
			} else {
				hashMap.remove(tail.value);
				tail = null;
				tail = newNode;				
			}
			
		} else {
			if(currentCapacity == 0) {
				head = newNode;
				tail = newNode;
			} else {
				newNode.previous = tail;
				tail.next = newNode;
				tail = newNode;				
			}
			currentCapacity++;
		}
		newNode.value = value;
		hashMap.put(key, newNode);
	}
	
	public static void main(String[] args) {
		LRUCache cache = new LRUCache(1);
		cache.set(2, 1);
		int i = cache.get(2);
		cache.set(3, 2);
		i = cache.get(2);
		i = cache.get(3);
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