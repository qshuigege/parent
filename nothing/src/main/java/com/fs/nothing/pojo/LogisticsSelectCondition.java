package com.fs.nothing.pojo;

public class LogisticsSelectCondition {

    private String  ERPUSEROID;//ERP用户订单号
    private String  PSODD;//ps单号
    private String  ERPUSERID;//ERP用户的id
    private String[]  ORDERSTATUS;
    private String  FSNO;
    private String  CUSTORDERNO;
    private Integer  MINONO;
    private Integer  MAXONO;

    public String getERPUSEROID() {
        return ERPUSEROID;
    }

    public void setERPUSEROID(String eRPUSEROID) {
        ERPUSEROID = eRPUSEROID;
    }

    public String getPSODD() {
        return PSODD;
    }

    public void setPSODD(String pSODD) {
        PSODD = pSODD;
    }

    public String getERPUSERID() {
        return ERPUSERID;
    }

    public void setERPUSERID(String eRPUSERID) {
        ERPUSERID = eRPUSERID;
    }

    public String[] getORDERSTATUS() {
        return ORDERSTATUS;
    }

    public void setORDERSTATUS(String[] oRDERSTATUS) {
        ORDERSTATUS = oRDERSTATUS;
    }

    public String getFSNO() {
        return FSNO;
    }

    public void setFSNO(String fSNO) {
        FSNO = fSNO;
    }

    public String getCUSTORDERNO() {
        return CUSTORDERNO;
    }

    public void setCUSTORDERNO(String cUSTORDERNO) {
        CUSTORDERNO = cUSTORDERNO;
    }

    public Integer getMINONO() {
        return MINONO;
    }

    public void setMINONO(Integer mINONO) {
        MINONO = mINONO;
    }

    public Integer getMAXONO() {
        return MAXONO;
    }

    public void setMAXONO(Integer mAXONO) {
        MAXONO = mAXONO;
    }


}
