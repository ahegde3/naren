package com.narren.leetCode;
/**
 * 
You need to construct a string consists of parenthesis and integers from a binary tree with the preorder traversing way.

The null node needs to be represented by empty parenthesis pair "()". And you need to omit all the empty parenthesis pairs that don't affect the one-to-one mapping relationship between the string and the original binary tree.

Example 1:
Input: Binary tree: [1,2,3,4]
       1
     /   \
    2     3
   /    
  4     

Output: "1(2(4))(3)"

Explanation: Originallay it needs to be "1(2(4)())(3()())", 
but you need to omit all the unnecessary empty parenthesis pairs. 
And it will be "1(2(4))(3)".
Example 2:
Input: Binary tree: [1,2,3,null,4]
       1
     /   \
    2     3
     \  
      4 

Output: "1(2()(4))(3)"

Explanation: Almost the same as the first example, 
except we can't omit the first parenthesis pair to break the one-to-one mapping relationship between the input and the output.
 * 
 * @author naren
 *
 */
public class ConstructStringfromBinaryTree {
	public static void main(String[] args) {
		TreeNode t1 = new TreeNode(1);
		TreeNode t2 = new TreeNode(2);
		TreeNode t3 = new TreeNode(3);
		TreeNode t4 = new TreeNode(4);
//		TreeNode t1 = new TreeNode(1);
//		
//		TreeNode t1 = new TreeNode(1);
		t2.right = t4;
		t1.left = t2;
		t1.right = t3;
		ConstructStringfromBinaryTree cbt = new ConstructStringfromBinaryTree();
		
		System.out.println(cbt.tree2str(t1));
		
	}
	String res = "";
	public String tree2str(TreeNode t) {
		
		makeString(t);
		return res;
	}
	String makeString(TreeNode t) {
		if(t == null) {
			return null;
		}
		res += "" + t.val;
		if(t.left == null && t.right == null) {
			return null;
		}
		res += "(";
		String left = makeString(t.left);
		if(left == null) {
			res += ")";
		} else {
			res += "(" + left;
		}
		if(t.right == null) {
			return null;
		}
		res += "(";
		String right = makeString(t.right);
		if(right == null) {
			res += ")";
		} else {
			res += "(" + right;
		}
		
		return null;
	}
}
