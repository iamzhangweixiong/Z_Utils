package com.zhangwx.z_utils.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 二叉树遍历：先序、中序、后序
 * https://www.jianshu.com/p/456af5480cee
 */
public class TreeTraversal {

    private List<Integer> orderList = new ArrayList<>();

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public List<Integer> inorderTraversal(TreeNode root) {

        if (root == null) {
            return orderList;
        }

        // 递归遍历
        orderTraversalFirst(root);
        orderTraversalMid(root);
        orderTraversalLast(root);

        // 非递归遍历
        orderTraversalFirst2(root);
        orderTraversalMid2(root);
        orderTraversalLast2(root);

        return orderList;
    }


    /**
     * 先序遍历：根节点 -> 左节点 -> 右节点
     */
    private void orderTraversalFirst(TreeNode node) {
        if (node == null) {
            return;
        }
        orderList.add(node.val);
        orderTraversalFirst(node.left);
        orderTraversalFirst(node.right);
    }

    private void orderTraversalFirst2(TreeNode root) {

        // 用来暂存节点的栈
        Stack<TreeNode> treeNodeStack = new Stack<TreeNode>();
        // 新建一个游标节点为根节点
        TreeNode node = root;
        // 当遍历到最后一个节点的时候，无论它的左右子树都为空，并且栈也为空
        // 所以，只要不同时满足这两点，都需要进入循环
        while (node != null || !treeNodeStack.isEmpty()) {
            // 若当前考查节点非空，则输出该节点的值
            // 由考查顺序得知，需要一直往左走
            while (node != null) {
                System.out.print(node.val + " ");
                // 为了之后能找到该节点的右子树，暂存该节点
                treeNodeStack.push(node);
                node = node.left;
            }
            // 一直到左子树为空，则开始考虑右子树
            // 如果栈已空，就不需要再考虑
            // 弹出栈顶元素，将游标等于该节点的右子树
            if (!treeNodeStack.isEmpty()) {
                node = treeNodeStack.pop();
                node = node.right;
            }
        }
    }


    /**
     * 中序遍历：左节点 -> 根节点 -> 右节点
     */
    private void orderTraversalMid(TreeNode node) {
        if (node == null) {
            return;
        }
        orderTraversalMid(node.left);
        orderList.add(node.val);
        orderTraversalMid(node.right);
    }

    private void orderTraversalMid2(TreeNode root) {

        Stack<TreeNode> treeNodeStack = new Stack<>();

        TreeNode node = root;

        while (node != null || !treeNodeStack.empty()) {

            while (node != null) {
                treeNodeStack.push(node);
                node = node.left;
            }

            if (!treeNodeStack.empty()) {
                node = treeNodeStack.pop();
                orderList.add(0, node.val);
                node = node.right;
            }
        }
    }

    /**
     * 后序遍历：左节点 -> 右节点 -> 根节点
     */
    private void orderTraversalLast(TreeNode node) {
        if (node == null) {
            return;
        }
        orderTraversalLast(node.left);
        orderTraversalLast(node.right);
        orderList.add(node.val);
    }

    private void orderTraversalLast2(TreeNode root) {

        Stack<TreeNode> treeNodeStack = new Stack<>();
        TreeNode node = root;
        TreeNode lastVisit = root;

        while (node != null || !treeNodeStack.empty()) {

            while (node != null) {
                treeNodeStack.push(node);
                node = node.left;
            }

            //查看当前栈顶元素
            node = treeNodeStack.peek();
            //如果其右子树也为空，或者右子树已经访问
            //则可以直接输出当前节点的值
            if (node.right == null || node.right == lastVisit) {
                orderList.add(0, node.val);
                treeNodeStack.pop();
                lastVisit = node;
                node = null;
            } else {
                //否则，继续遍历右子树
                node = node.right;
            }
        }
    }

    public static void main(String[] args) {

    }
}
