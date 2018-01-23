package com.narren.gayle.treeAndGraph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.narren.leetCode.TreeNode;

import sun.misc.Queue;

/**
 * Returns a list of linked list for each level
 * @author naren
 *
 */
public class ListOfDepths {

	List<LinkedList<TreeNode>> getList(TreeNode root) {
		if(root == null) {
			return null;
		}

		List<LinkedList<TreeNode>> list = new ArrayList<LinkedList<TreeNode>>();
		Queue<NodeQ> queue = new Queue<NodeQ>();

		NodeQ node = new NodeQ(root, 0);

		while(!queue.isEmpty()) {
			NodeQ n = null;
			try {
				n = queue.dequeue();
				LinkedList<TreeNode> head = list.get(n.level);
				if(head == null) {
					LinkedList<TreeNode> llist = new LinkedList<TreeNode>();
					llist.add(n.node);
					list.add(n.level, llist);
				} else {
					head.add(n.node);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(n.node.left != null) {
				queue.enqueue(new NodeQ(n.node.left, n.level + 1));
			}
			if(n.node.right != null) {
				queue.enqueue(new NodeQ(n.node.right, n.level + 1));
			}

		}
		return list;

	}
}

class NodeQ {
	TreeNode node;
	int level;

	public NodeQ(TreeNode n, int l) {
		node = n;
		level = l;
	}
}
