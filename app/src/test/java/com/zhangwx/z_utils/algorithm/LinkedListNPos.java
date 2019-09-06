package com.zhangwx.z_utils.algorithm;

/**
 * 找出单链表倒数第 n 个数
 * <p>
 * 6 -> 5 -> 4 -> 3 -> 2 -> 1 -> 0
 * ^
 * n = 7
 * </p>
 *
 * <p>
 *     思路：用两个指针，一个指向遍历的顺序，另一个和前一个保持 n 的距离
 * </p>
 */
public class LinkedListNPos {

    static class Node {
        int value;
        Node next;

        Node(int value, Node next) {
            this.next = next;
            this.value = value;
        }
    }

    public Node getNode(int n, Node header) {
        int i = 0;
        Node node = header;// 指针，作用是遍历，指向最新的节点
        Node targetNode = null;// 目标指针，指向目标节点，和前面的指针间隔为 n
        while (node.next != null) {
            i++;
            node = node.next;
            if (i == n) {//
                targetNode = header;
            }
            if (targetNode != null) {
                targetNode = targetNode.next;
            }
        }
        return targetNode;
    }

    public static void main(String[] args) {

        Node node0 = new Node(0, null);
        Node node1 = new Node(1, node0);
        Node node2 = new Node(2, node1);
        Node node3 = new Node(3, node2);
        Node node4 = new Node(4, node3);
        Node node5 = new Node(5, node4);
        Node header = new Node(6, node5);

        LinkedListNPos pos = new LinkedListNPos();
        System.out.println(pos.getNode(4, header).value);
    }
}
