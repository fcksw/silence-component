package com.silence.mvc;

import com.silence.mvc.algorithm.ListNode;

public class Solution {


    /**
     *  2、两数相加
     *
     *  给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，
     *  并且每个节点只能存储 一位 数字。
     *
     * 请你将两个数相加，并以相同形式返回一个表示和的链表。
     *
     * 你可以假设除了数字 0 之外，这两个数都不会以 0 开头。
     *
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        if (l1 == null || l2 == null) {
            return l1 == null ? l2 : l1;
        }

        ListNode head = null, tail = null;
        //存储进位
        int temp = 0;
        while (l1 != null || l2 != null) {
            int l1Val = l1 != null ? l1.val : 0;
            int l2Val = l2 != null ? l2.val : 0;

            if (head == null) {
                head = tail = new ListNode((l1Val + l2Val + temp) % 10);
            }else {
                tail.next = new ListNode((l1Val + l2Val + temp) % 10 );
                tail = tail.next;
            }
            temp = (l1Val + l2Val + temp) / 10;

            l1 = l1 != null ? l1.next : null;

            l2 = l2 != null ? l2.next : null;
        }
        if (temp > 0) {
            tail.next = new ListNode(temp);
        }
        return head;
    }


}
