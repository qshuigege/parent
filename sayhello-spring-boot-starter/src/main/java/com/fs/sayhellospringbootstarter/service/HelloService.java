package com.fs.sayhellospringbootstarter.service;

public class HelloService {
    private String name;

    public String sayHello(){
        return "hello, " + name;
    }

    public void setName(String name){
        this.name = name;
    }
}
