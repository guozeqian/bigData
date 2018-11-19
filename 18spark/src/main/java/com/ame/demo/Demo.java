package com.ame.demo;


/**
 * 数组反转
 * jiane
 * 2018/11/19 17:58
 */
public class Demo {
    public static void main(String[] args) {
        String[] a = {"1", "2", "3"};
        String[] newArray = new String[a.length];
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = a[a.length - i - 1];
        }
        for (String b : newArray) {
            System.out.println(b);
        }
    }
}
