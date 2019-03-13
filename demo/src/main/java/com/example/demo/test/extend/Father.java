package com.example.demo.test.extend;

public abstract class Father {
    private int aaa;
    public int bbb;
    public String ccc;

    public Father(int aaa){
        this.aaa = aaa;
    }
    public Father(int aaa, int bbb){
        this.aaa = aaa;
        this.bbb = bbb;
    }

    public abstract int getANumber();

    public int getAaa() {
        return aaa;
    }

    public void setAaa(int aaa) {
        this.aaa = aaa;
    }

    public int getBbb() {
        return bbb;
    }

    public void setBbb(int bbb) {
        this.bbb = bbb;
    }
}
