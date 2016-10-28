package com.narren.tree;

public class Node {
	Node left;
	Node right;
	int data;
	
	Node(int item) {
        data = item;
        left = right = null;
    }
}
