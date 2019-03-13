package com.example.demo.service.impl;


import com.example.demo.repository.WXBusinessDao;
import com.example.demo.service.WXBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WXBusinessServiceImpl implements WXBusinessService {

    @Autowired
    private WXBusinessDao dao;

    /*@Override
    public void addAcc(WXUserInfo userInfo) {

    }

    @Override
    public void addAcc(WXAccount wxAccount) {
        dao.addWXAcc(wxAccount);
    }*/

    @Override
    public boolean isBinded(String unionid) {

        return dao.isBinded(unionid);
    }

    @Override
    public boolean unbinding(String unionid) {
        return dao.unbinding(unionid);
    }

    @Override
    public boolean binding(String unionid, String orgid) {
        return dao.binding(unionid, orgid);
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
    public List<B2B_USER> getOrguserByUnionid(String unionid) throws Exception {
        return dao.getOrguserByUnionid(unionid);
    }*/

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
    public List<Map<String, Object>> getTHInfo(Map<String, Object> params) throws Exception {
        return dao.getTHInfo(params);
    }

   /* @Override
    public List<Map<String, Object>> getOrderMobileWeb(String erpuseroid, String minono, String maxono) {
        return dao.getOrderMobileWeb(erpuseroid, minono, maxono);
    }*/
}
