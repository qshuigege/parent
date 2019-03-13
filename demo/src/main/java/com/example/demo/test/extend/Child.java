package com.example.demo.test.extend;

public class Child extends Father {
    private int aaa = 555;
    public String name;
    public int bbb;
    public String ccc;
    public Child(int aaa) {
        super(aaa);
        this.bbb = 10;
        super.bbb = 100;
        this.bbb = 5;
        this.ccc = "ccc";
        super.ccc = "CCC";
    }

    public void test(){
        System.out.println("super.bbb-->"+super.bbb);
        System.out.println("this.bbb-->"+this.bbb);
        System.out.println("this.getAaa()-->"+this.getAaa());
        System.out.println("super.getAaa()-->"+super.getAaa());
        System.out.println("this.getANumber()-->"+this.getANumber());
        System.out.println("this.ccc-->"+this.ccc);
        System.out.println("super.ccc-->"+super.ccc);
    }

    @Override
    public int getAaa(){
        return this.aaa;
    }

    public int getANumber(){
        return 888;
    }

    public static void main(String[] args){
        Child c = new Child(999);
        c.test();
    }
}

