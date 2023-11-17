package com.doyoulikemi4i.nacosconsumer.po;

public class Payment {
    public String money;
    public Payment(){}
    public Payment(String money){
        this.money = money;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
