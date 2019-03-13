package com.example.demo.test.annotation;

import com.example.demo.test.enum_test.Priority;
import com.example.demo.test.enum_test.Status;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface MyController {

    String value() default "hello";

    Priority priority() default Priority.LOW;

    Status status() default Status.NOT_STARTED;

    Class<? extends Exception> exception() default RuntimeException.class;
}
