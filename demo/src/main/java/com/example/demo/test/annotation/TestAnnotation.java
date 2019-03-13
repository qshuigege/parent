package com.example.demo.test.annotation;

import com.example.demo.test.enum_test.Priority;

@MyController(priority = Priority.MEDIUM, value = "haha")
public class TestAnnotation {

    @MyAutowired
    public String name;
}
