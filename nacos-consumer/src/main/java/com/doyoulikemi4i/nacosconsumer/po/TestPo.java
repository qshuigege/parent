package com.doyoulikemi4i.nacosconsumer.po;

public class TestPo {
    private String name;
    private int age;

    public TestPo(){};

    public TestPo(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
