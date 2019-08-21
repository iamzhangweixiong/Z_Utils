package com.zhangwx.z_utils.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * N 叉树最大深度
 */
public class MaxDepth {

    public Node genNode() {
        Node fourth_1 = new Node(9, null);
        List<Node> fourthList = new ArrayList<>();
        fourthList.add(fourth_1);

        Node third_1 = new Node(8, fourthList);
        List<Node> thirdList = new ArrayList<>();
        thirdList.add(third_1);

        Node third_2 = new Node(7, fourthList);
        List<Node> third2List = new ArrayList<>();
        third2List.add(third_2);

        Node second_3 = new Node(6, null);
        Node second_2 = new Node(5, third2List);
        Node second_1 = new Node(4, thirdList);
        List<Node> secondList = new ArrayList<>();
        secondList.add(second_1);
        secondList.add(second_2);
        secondList.add(second_3);

        Node first_2 = new Node(3, null);
        Node first_1 = new Node(2, secondList);
        List<Node> firstList = new ArrayList<>();
        firstList.add(first_1);
        firstList.add(first_2);

        return new Node(1, firstList);
    }

    class Node {
        public int val;
        public List<Node> children;

        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    }

    int count = 0;

    public int maxDepth(Node root) {
        search(root, 1);
        return count;
    }

    private void search(Node node, int depth) {
        if (node.children == null) {
            return;
        }
        count = depth;
        for (Node child : node.children) {
            search(child, depth + 1);
        }

    }

    public static void main(String[] args) {
        MaxDepth max = new MaxDepth();
        System.out.println(max.maxDepth(max.genNode()));
    }
}
