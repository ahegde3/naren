package com.narren.tree;

/**
 * Prints the level order traversal
 * @author naren
 *
 */
public class LevelOrderTraversal {


	void printLevelOrder(Node node) {
		if(node == null) {
			return;
		}
		Queue q = new Queue(10);
		q.enqueue(node);
		
		while(!q.isQueueEmpty()) {
			Node n = q.dequeue();
			System.out.println(n.data);
			
			if(n.left != null) {
				q.enqueue(n.left);
			}
			
			if(n.right != null) {
				q.enqueue(n.right);
			}
		}
	}
}