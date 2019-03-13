package com.fs.nothing.repository;

import com.fs.nothing.pojo.B2B_USER;
import com.fs.nothing.wx.apimodel.WXAccount;
import com.fs.nothing.wx.apimodel.WXUserInfo;

import java.util.List;
import java.util.Map;

public interface WXBusinessDao {

    void addWXAcc(WXAccount acc);

    boolean isBinded(String unionid, String openid);

    boolean binding(String unionid, String orgid, String openid);

    String getOrgidByUniqueid(String uniqueid) throws Exception;

    /*boolean unbinding(String unionid, String openid);*/
    boolean unbinding(Map<String, Object> params);

    int unsubscribe(String openid);

    List<Map<String,Object>> getOrderMobileWeb(String erpuseroid, String minono, String maxono, String pageSize, String rowIndex) throws Exception;

    List<Map<String, Object>> getLogisticMobileWeb(String orda001oid) throws Exception;

    List<Map<String, Object>> getOrderAndLogistics(String orda001oid) throws Exception;

    List<Map<String, Object>> getConditionOrder(String erpuseroid, String[] orderStatus, String fsno, String custOrderNo, String pageSize, String rowIndex)throws Exception;

    Map<String,Object> getErpuseroidByOpenid(String openid)throws Exception;

    Map<String,Object> getUnionidByOpenid(String openid) throws Exception;

    /*List<B2B_USER> getOrguserByOpenid(String unionid, String openid) throws Exception;*/
    List<B2B_USER> getOrguserByUnionid(Map<String, Object> params) throws Exception;

    int updateLastLoginTime(String orgid) throws Exception;

    List<Map<String,Object>> getBindedOpenidsByErpoid(String erpoid);

    List<Map<String,Object>> getBindedWXAccs(String orgid) throws Exception;

    int unbindWXAccsBatch(String[] unionids) throws Exception;

    int[] updateHeadImg(List<WXUserInfo> userList) throws Exception;
}
