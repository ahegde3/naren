package com.narren.leetCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BinaryTreeLevelOrderTraversal_1 {
    public List<List<Integer>> levelOrder(TreeNode root) {
        
    	int height = getHeight(root, 1);
        List<List<Integer>> mainList = new ArrayList<>();
        for(int i = 0; i < height; i++) {
        	List<Integer> list = new ArrayList<>();
        	mainList.add(list);
        }
        
        level(mainList, root, 0);
        
        return mainList;
    }
    
    void level(List<List<Integer>> mainList, TreeNode node, int level) {
    	if(node == null) {
    		return;
    	}
    	
    	level(mainList, node.left, level + 1);
    	level(mainList, node.right, level + 1);
    	
    	List<Integer> list = mainList.get(level);
		list.add(node.val);
    }
    
    int getHeight(TreeNode node, int h) {
    	if(node == null) {
    		return 0;
    	}
    	int leftHeight = getHeight(node.left, h + 1);
    	int rightHeight = getHeight(node.right, h + 1);
    	
    	return Math.max(Math.max(leftHeight, rightHeight), h);
    }
    
    
    

	public static void main(String[] args) throws IOException {
		TreeNode root = new TreeNode(3);
		
		TreeNode node9 = new TreeNode(9);
		node9.left = null;
		node9.right = null;
		
		TreeNode node15 = new TreeNode(15);
		node15.left = null;
		node15.right = null;

		TreeNode node7 = new TreeNode(7);
		node7.left = null;
		node7.right = null;
		
		
		TreeNode node20 = new TreeNode(20);
		node20.left = node15;
		node20.right = node7;
		
		root.left = node9;
		root.right = node20;
		
		new BinaryTreeLevelOrderTraversal_1().levelOrder(root);
		
	}
    
}
