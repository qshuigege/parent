package com.fs.everything.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class B2B_USER implements Serializable {

    /**
     *用户ID
     */
    private  String  f_ID;
    /**
     *注册时间
     */
    private Date f_ZCSJ;
    /**
     *用户公司名称
     */
    private  String  f_GSMC;
    /**
     *用户类型
     */
    private  int  f_YHLX;
    /**
     *用户名称
     */
    private  String  f_YHMC;
    /**
     *用户账号
     */
    private  String  f_YHZH;
    /**
     *用户密码
     */
    private  String  f_YHMM;
    /**
     *手机
     */
    private  String  f_LXRSJ;
    /**
     *Email
     */
    private  String  f_LXRMAIL;
    /**
     *备注
     */
    private  String  f_BZ;
    /**
     *客户外键
     */
    private  String  f_KH_FK;
    /**
     *微信号ID
     */
    private  String  f_WX_OPENID;
    /**
     * QQID
     */
    private String  f_QQ_OPENID;
    /**
     * 修改时间
     */
    private Date f_XGSJ;
    /**
     * 最后登录时间
     */
    private Date  f_ZHDLSJ;


    private String f_ROLE;


    private  String  f_FSKHBM;

    private char miniprogflag;

    private String qywxuserid;

    private String qywxnickname;

    private String qywxavatar;

    private String loginid;

    private String empnum;

    private String ucml_useroid;

    private String ucml_department_name;

    private String ucml_zrzx;

    private String ucml_audit;

    private Map<String, Object> extra;

    public B2B_USER(){}

    public String getF_ID() {
        return f_ID;
    }

    public void setF_ID(String f_ID) {
        this.f_ID = f_ID;
    }

    public Date getF_ZCSJ() {
        return f_ZCSJ;
    }

    public void setF_ZCSJ(Date f_ZCSJ) {
        this.f_ZCSJ = f_ZCSJ;
    }

    public String getF_GSMC() {
        return f_GSMC;
    }

    public void setF_GSMC(String f_GSMC) {
        this.f_GSMC = f_GSMC;
    }

    public int getF_YHLX() {
        return f_YHLX;
    }

    public void setF_YHLX(int f_YHLX) {
        this.f_YHLX = f_YHLX;
    }

    public String getF_YHMC() {
        return f_YHMC;
    }

    public void setF_YHMC(String f_YHMC) {
        this.f_YHMC = f_YHMC;
    }

    public String getF_YHZH() {
        return f_YHZH;
    }

    public void setF_YHZH(String f_YHZH) {
        this.f_YHZH = f_YHZH;
    }

    public String getF_YHMM() {
        return f_YHMM;
    }

    public void setF_YHMM(String f_YHMM) {
        this.f_YHMM = f_YHMM;
    }

    public String getF_LXRSJ() {
        return f_LXRSJ;
    }

    public void setF_LXRSJ(String f_LXRSJ) {
        this.f_LXRSJ = f_LXRSJ;
    }

    public String getF_LXRMAIL() {
        return f_LXRMAIL;
    }

    public void setF_LXRMAIL(String f_LXRMAIL) {
        this.f_LXRMAIL = f_LXRMAIL;
    }

    public String getF_BZ() {
        return f_BZ;
    }

    public void setF_BZ(String f_BZ) {
        this.f_BZ = f_BZ;
    }

    public String getF_KH_FK() {
        return f_KH_FK;
    }

    public void setF_KH_FK(String f_KH_FK) {
        this.f_KH_FK = f_KH_FK;
    }

    public String getF_WX_OPENID() {
        return f_WX_OPENID;
    }

    public void setF_WX_OPENID(String f_WX_OPENID) {
        this.f_WX_OPENID = f_WX_OPENID;
    }

    public String getF_QQ_OPENID() {
        return f_QQ_OPENID;
    }

    public void setF_QQ_OPENID(String f_QQ_OPENID) {
        this.f_QQ_OPENID = f_QQ_OPENID;
    }

    public Date getF_XGSJ() {
        return f_XGSJ;
    }

    public void setF_XGSJ(Date f_XGSJ) {
        this.f_XGSJ = f_XGSJ;
    }

    public Date getF_ZHDLSJ() {
        return f_ZHDLSJ;
    }

    public void setF_ZHDLSJ(Date f_ZHDLSJ) {
        this.f_ZHDLSJ = f_ZHDLSJ;
    }

    public String getF_ROLE() {
        return f_ROLE;
    }

    public void setF_ROLE(String f_ROLE) {
        this.f_ROLE = f_ROLE;
    }

    public String getF_FSKHBM() {
        return f_FSKHBM;
    }

    public void setF_FSKHBM(String f_FSKHBM) {
        this.f_FSKHBM = f_FSKHBM;
    }

    public char getMiniprogflag() {
        return miniprogflag;
    }

    public void setMiniprogflag(char miniprogflag) {
        this.miniprogflag = miniprogflag;
    }

    public String getQywxuserid() {
        return qywxuserid;
    }

    public void setQywxuserid(String qywxuserid) {
        this.qywxuserid = qywxuserid;
    }

    public String getQywxnickname() {
        return qywxnickname;
    }

    public void setQywxnickname(String qywxnickname) {
        this.qywxnickname = qywxnickname;
    }

    public String getQywxavatar() {
        return qywxavatar;
    }

    public void setQywxavatar(String qywxavatar) {
        this.qywxavatar = qywxavatar;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getEmpnum() {
        return empnum;
    }

    public void setEmpnum(String empnum) {
        this.empnum = empnum;
    }

    public String getUcml_useroid() {
        return ucml_useroid;
    }

    public void setUcml_useroid(String ucml_useroid) {
        this.ucml_useroid = ucml_useroid;
    }

    public String getUcml_department_name() {
        return ucml_department_name;
    }

    public void setUcml_department_name(String ucml_department_name) {
        this.ucml_department_name = ucml_department_name;
    }

    public String getUcml_zrzx() {
        return ucml_zrzx;
    }

    public void setUcml_zrzx(String ucml_zrzx) {
        this.ucml_zrzx = ucml_zrzx;
    }

    public String getUcml_audit() {
        return ucml_audit;
    }

    public void setUcml_audit(String ucml_audit) {
        this.ucml_audit = ucml_audit;
    }
}
