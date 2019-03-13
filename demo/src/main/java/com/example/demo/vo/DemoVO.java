package com.example.demo.vo;

import java.math.BigDecimal;
import java.util.Date;

public class DemoVO {

    private String aaa;
    private int bbb;
    private Date ccc;
    private BigDecimal ddd;
    private Double eee;
    private char fff;

    public DemoVO(){}

    public DemoVO(String aaa, int bbb, Date ccc, BigDecimal ddd, Double eee, char fff) {
        this.aaa = aaa;
        this.bbb = bbb;
        this.ccc = ccc;
        this.ddd = ddd;
        this.eee = eee;
        this.fff = fff;
    }

    public String getAaa() {
        return aaa;
    }

    public void setAaa(String aaa) {
        this.aaa = aaa;
    }

    public int getBbb() {
        return bbb;
    }

    public void setBbb(int bbb) {
        this.bbb = bbb;
    }

    public Date getCcc() {
        return ccc;
    }

    public void setCcc(Date ccc) {
        this.ccc = ccc;
    }

    public BigDecimal getDdd() {
        return ddd;
    }

    public void setDdd(BigDecimal ddd) {
        this.ddd = ddd;
    }

    public Double getEee() {
        return eee;
    }

    public void setEee(Double eee) {
        this.eee = eee;
    }

    public char getFff() {
        return fff;
    }

    public void setFff(char fff) {
        this.fff = fff;
    }
}
