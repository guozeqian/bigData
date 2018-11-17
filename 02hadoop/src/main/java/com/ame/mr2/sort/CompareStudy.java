package com.ame.mr2.sort;

public class CompareStudy {
    public static void main(String[] args) {
        String first = "xlksdjlfsjldfkjslkdf";
        String second = "bbb";
        //如果第一个比第二个大返回正数
        //如果第一个和第二个相等  返回  0
        //如果第一个比第二个小  返回  负数
        Integer integer = new Integer(10);
        Integer integer2 = new Integer(1);

        System.out.println(integer2.compareTo(integer));

        int i = first.compareTo(second);
        System.out.println(i);


    }

}
