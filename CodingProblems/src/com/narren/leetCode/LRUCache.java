package com.narren.leetCode;

import java.util.HashMap;

public class LRUCache {

	HashMap<Integer, DLList> map;
	int capacity;
	int curCapacity;

	DLList head;
	DLList tail;

	public LRUCache(int capacity) {
		this.capacity = capacity;
		map = new HashMap<>();
		
		head = new DLList();
		tail = new DLList();
		
		head.after = tail;
		tail.before = head;
	}

	// Moves this node to starting of the list
	void moveToFirst(DLList node) {
		head.after.before = node;
		node.after = head.after;
		node.before = head;
		head.after = node;
	}

	// Removes the current connection of this node
	void remove(DLList node) {
		node.before.after = node.after;
		node.after.before = node.before;
	}    

	// Remove this node, because we ran out of space
	void removeTail() {
		map.remove(tail.before.key, tail.before);
		remove(tail.before);
		curCapacity--;
	}

	// Insert a new node, and make it head
	void insertNode(DLList node) {		
		moveToFirst(node);
		map.put(node.key, node);

		curCapacity++;
	}

	public int get(int key) {
		if(!map.containsKey(key))
			return -1;

		DLList node = map.get(key);
		int retVal = node.val;

		if(node.before != head) {
			remove(node);
			moveToFirst(node);    
		}


		return retVal;
	}

	public void put(int key, int value) {
		if(map.containsKey(key)) {
			// Existing item
			DLList node = map.get(key);
			node.val = value;

			remove(node);
			moveToFirst(node);

		} else {
			// New item
			DLList node = new DLList();
			node.key = key;
			node.val = value;

			if(curCapacity >= capacity)
				removeTail();

			insertNode(node);

		}
	}


	public static void main(String[] args) {
		LRUCache cache = new LRUCache(2);

		cache.put(1,  1);
		cache.put(2,  2);

		System.out.println(cache.get(1));
		cache.put(3,  3);

		System.out.println(cache.get(2));
		cache.put(4,  4);

		System.out.println(cache.get(1));
		System.out.println(cache.get(3));
		System.out.println(cache.get(4));


	}
}

class DLList {
	int key;
	int val;
	DLList before;
	DLList after;

}