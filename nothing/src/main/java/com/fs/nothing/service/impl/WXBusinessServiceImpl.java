package com.fs.nothing.service.impl;


import com.fs.nothing.pojo.B2B_USER;
import com.fs.nothing.repository.WXBusinessDao;
import com.fs.nothing.service.WXBusinessService;
import com.fs.nothing.wx.apimodel.WXAccount;
import com.fs.nothing.wx.apimodel.WXUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WXBusinessServiceImpl implements WXBusinessService {

    @Autowired
    private WXBusinessDao dao;

    @Override
    public void addAcc(WXUserInfo userInfo) {

    }

    @Override
    public void addAcc(WXAccount wxAccount) {
        dao.addWXAcc(wxAccount);
    }

    @Override
    public boolean isBinded(String unionid, String openid) {

        return dao.isBinded(unionid, openid);
    }

    /*@Override
    public boolean unbinding(String unionid, String openid) {
        return dao.unbinding(unionid, openid);
    }*/

    @Override
    public boolean unbinding(Map<String, Object> params) {
        return dao.unbinding(params);
    }

    @Override
    public boolean binding(String unionid, String orgid, String openid) {
        return dao.binding(unionid, orgid, openid);
    }

    @Override
    public int unsubscribe(String openid) {
        return dao.unsubscribe(openid);
    }

    @Override
    public String getOrgidByUniqueid(String uniqueid) throws Exception{
        return dao.getOrgidByUniqueid(uniqueid);
    }

    @Override
    public List<Map<String, Object>> getOrderMobileWeb(String erpuseroid, String minono, String maxono, String pageSize, String rowIndex) throws Exception {
        return dao.getOrderMobileWeb(erpuseroid, minono, maxono, pageSize, rowIndex);
    }

    @Override
    public List<Map<String, Object>> getLogisticMobileWeb(String erpuseroid) throws Exception {
        return dao.getLogisticMobileWeb(erpuseroid);
    }

    @Override
    public List<Map<String, Object>> getOrderAndLogistics(String erpuseroid) throws Exception {
        return dao.getOrderAndLogistics(erpuseroid);
    }

    @Override
    public List<Map<String, Object>> getConditionOrder(String erpuseroid, String[] orderStatus, String fsno, String custOrderNo, String pageSize, String rowIndex) throws Exception {
        return dao.getConditionOrder(erpuseroid, orderStatus, fsno, custOrderNo, pageSize, rowIndex);
    }

    @Override
    public Map<String, Object> getErpuseroidByOpenid(String openid) throws Exception{
        return dao.getErpuseroidByOpenid(openid);
    }

    @Override
    public Map<String, Object> getUnionidByOpenid(String openid) throws Exception {
        return dao.getUnionidByOpenid(openid);
    }

    /*@Override
    public List<B2B_USER> getOrguserByOpenid(String unionid, String openid) throws Exception {
        return dao.getOrguserByOpenid(unionid, openid);
    }*/

    @Override
    public List<B2B_USER> getOrguserByUnionid(Map<String, Object> params) throws Exception {
        return dao.getOrguserByUnionid(params);
    }

    @Override
    public int updateLastLoginTime(String orgid) throws Exception {
        return dao.updateLastLoginTime(orgid);
    }

    @Override
    public List<Map<String, Object>> getBindedOpenidsByErpoid(String erpoid) {
        return dao.getBindedOpenidsByErpoid(erpoid);
    }

    @Override
    public List<Map<String, Object>> getBindedWXAccs(String orgid) throws Exception {
        return dao.getBindedWXAccs(orgid);
    }

    @Override
    public int unbindWXAccsBatch(String[] unionids) throws Exception {
        return dao.unbindWXAccsBatch(unionids);
    }

    @Override
    public int[] updateHeadImg(List<WXUserInfo> userList) throws Exception {
        return dao.updateHeadImg(userList);
    }

   /* @Override
    public List<Map<String, Object>> getOrderMobileWeb(String erpuseroid, String minono, String maxono) {
        return dao.getOrderMobileWeb(erpuseroid, minono, maxono);
    }*/
}
