package com.fs.everything.service;

import com.fs.everything.pojo.B2B_USER;
import com.fs.everything.pojo.WXAccount;

import java.util.List;
import java.util.Map;

public interface WXBusinessService {

    boolean isBinded(String unionid, String openid);

    /*List<B2B_USER> getOrguserByOpenid(String unionid, String openid) throws Exception;*/
    List<B2B_USER> getOrguserByUnionid(Map<String, Object> params) throws Exception;

    List<B2B_USER> getOrguserByOrgid(Map<String, Object> params) throws Exception;

    List<B2B_USER> getOrguserByUserid(Map<String, Object> params) throws Exception;

    int addMiniprogAcc(WXAccount acc) throws Exception;

    boolean isAccExist(String openid);

    Map<String,Object> getPSAddr(String psno) throws Exception;

    int doshconfirm(String shpc002oid, String psno, String confirmplace, String pics, String remark, String orgid) throws Exception;

    List<Map<String,Object>> getTHInfo(String thno) throws Exception;

    int saveStepData(String shpc002oid, String sth_cur_step, String docno, String step_place, String pics, String remark, String orgid) throws Exception;

    /*int dothconfirm(String shpc002oid, String thno, String confirmplace, String pics, String remark, String orgid) throws Exception;*/

    int changeMiniprogflag(String miniprogflag, String orgid) throws Exception;

    /*List<Map<String,Object>> getBindedOpenidsByErpoid(String erpoid);*/

    boolean isNeedShconfirmMsg(String erpuseroid);

    boolean isOrguserExist(Map<String, Object> params);

    String addOrguserAccQywx(Map<String, Object> params) throws Exception;
}
