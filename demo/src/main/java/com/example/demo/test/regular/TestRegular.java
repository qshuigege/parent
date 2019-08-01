package com.example.demo.test.regular;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegular {

    public static void main(String[] args) {
        String str = "are you ok?, do you like mi 4i?";
        //Pattern.matches("you", str);//false,因为matches方法是完全匹配
        String expression = "like";

        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(str);
        boolean b1 = matcher.matches();//matches方法是完全匹配
        System.out.println(str);

        pattern = Pattern.compile(expression);
        matcher = pattern.matcher(str);
        boolean b2 = matcher.lookingAt();//lookingAt方法必须以表达式表达的内容开头

        pattern = Pattern.compile(expression);
        matcher = pattern.matcher(str);
        boolean b3 = matcher.find();//find方法只要字符串中包含了表达式表达的内容，就算匹配成功

        System.out.println(b1);
        System.out.println(b2);
        System.out.println(b3);
    }

}
