package com.zhangwx.z_utils.algorithm;

import java.util.ArrayList;
import java.util.List;

// 反转字符串
public class ReverseWord {

    public String reverseWords() {
        final String string = "Let's take LeetCode contest";
        final char empty = ' ';
        StringBuilder result = new StringBuilder();

        final int lenght = string.length();

        List<Character> charList = new ArrayList<>();

        for (int i = 0; i < lenght; i++) {
            final char c = string.charAt(i);
            final boolean lastOne = i == lenght - 1;
            if (c == empty) {
                charList.add(empty);
                for (Character ch : charList) {
                    result.append(ch);
                }
                charList.clear();
            } else {
                charList.add(0, c);
            }
            if (lastOne) {
                for (Character ch : charList) {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }


    // 第二种
    public String reverseWords2() {
        final String string = "Let's take LeetCode contest";
        String[] words = string.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(swapWord(0, word.length() - 1, word.toCharArray())).append(" ");
        }

        return sb.toString().trim();
    }

    public String swapWord(int s, int e, char[] c) {
        if (s >= e) {
            return String.valueOf(c);
        }

        char temp = c[s];
        c[s] = c[e];
        c[e] = temp;
        return swapWord(s + 1, e - 1, c);
    }

    public static void main(String[] args) {

    }
}
