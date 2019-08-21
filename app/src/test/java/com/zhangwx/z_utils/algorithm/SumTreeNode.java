package com.zhangwx.z_utils.algorithm;

/**
 * 二叉树遍历
 */
public class SumTreeNode {

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    private int sum = 0;

    public int sumRootToLeaf(TreeNode root) {
        dfs(root, 0);
        return sum;
    }

    // 遍历整棵二叉树
    private void dfs(TreeNode node, int val) {
        if (node == null) return;

        int newVal = val << 1 | node.val; // 父节点传入的值左移一位|本节点的值

        if (node.left == null && node.right == null) {
            sum += newVal;
        } else {
            dfs(node.left, newVal);
            dfs(node.right, newVal);
        }
    }

    public static void main(String[] args) {

    }
}
