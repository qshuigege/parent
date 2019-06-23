package com.example.demo.test.character;

public class Test {

    public static void main(String[] args) throws Exception{
        String str = "龘";
        //String str2 = "你";
        String str2 = "";
        String str3 = "一";
        byte[] b1 = str.getBytes("utf-8");
        byte[] b2 = str2.getBytes("utf-8");
        byte[] b3 = str3.getBytes("utf-8");
        System.out.println(b1.length);
        System.out.println(b2.length);
        System.out.println(b3.length);

        char c1 = '';
        char c2 = '龖';

    }

}
