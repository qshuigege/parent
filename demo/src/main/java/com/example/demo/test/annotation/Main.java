package com.example.demo.test.annotation;

import com.example.demo.test.enum_test.Priority;

import java.lang.reflect.Field;

public class Main {

    public static void main(String[] args) throws Exception{
        Class<?> clazz = Class.forName("com.example.demo.test.annotation.TestAnnotation");
        boolean annotationPresent = clazz.isAnnotationPresent(MyController.class);
        if (annotationPresent){
            System.out.println("true");
        }else {
            System.out.println("false");
        }

        Field name = clazz.getDeclaredField("name");
        boolean flag = name.isAnnotationPresent(MyAutowired.class);
        if (flag){
            System.out.println("true");
        }else {
            System.out.println("false");
        }

        Class<?> clazz2 = Class.forName("com.example.demo.test.annotation.TestAnnotationChild");
        clazz2 = TestAnnotationChild.class;
        boolean flag2 = clazz2.isAnnotationPresent(MyController.class);
        if (flag2){
            MyController annotation = clazz2.getAnnotation(MyController.class);
            Priority priority = annotation.priority();
            String value = annotation.value();
            System.out.println(priority==Priority.LOW);
            System.out.println(value);
            Class exception = annotation.exception();
            System.out.println(Exception.class.isAssignableFrom(exception));
            //System.out.println("true");
        }else {
            System.out.println("false");
        }

    }
}
