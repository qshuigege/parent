package com.fs.everything.service.impl;


import com.fs.everything.pojo.B2B_USER;
import com.fs.everything.pojo.WXAccount;
import com.fs.everything.repository.WXBusinessDao;
import com.fs.everything.service.WXBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WXBusinessServiceImpl implements WXBusinessService {

    @Autowired
    private WXBusinessDao dao;

    @Override
    public boolean isBinded(String unionid, String openid) {

        return dao.isBinded(unionid, openid);
    }

    /*@Override
    public List<B2B_USER> getOrguserByOpenid(String unionid, String openid) throws Exception {
        return dao.getOrguserByOpenid(unionid, openid);
    }*/

    @Override
    public List<B2B_USER> getOrguserByUnionid(Map<String, Object> params)throws Exception{
        return dao.getOrguserByUnionid(params);
    }

    @Override
    public List<B2B_USER> getOrguserByOrgid(Map<String, Object> params) throws Exception {
        return dao.getOrguserByOrgid(params);
    }

    @Override
    public List<B2B_USER> getOrguserByUserid(Map<String, Object> params) throws Exception {
        return dao.getOrguserByUserid(params);
    }

    @Override
    public int addMiniprogAcc(WXAccount acc) throws Exception{
        return dao.addMiniprogAcc(acc);
    }

    @Override
    public boolean isAccExist(String openid) {
        return dao.isAccExist(openid);
    }

    @Override
    public Map<String, Object> getPSAddr(String psno) throws Exception {
        return dao.getPSAddr(psno);
    }

    @Override
    public int doshconfirm(String shpc002oid, String psno, String confirmplace, String pics, String remark,String orgid) throws Exception {
        return dao.doshconfirm(shpc002oid, psno, confirmplace, pics, remark);
    }

    @Override
    public List<Map<String, Object>> getTHInfo(String thno) throws Exception {
        return dao.getTHInfo(thno);
    }

    @Override
    public int saveStepData(String shpc002oid, String sth_cur_step, String docno, String step_place, String pics, String remark, String orgid) throws Exception {
        return dao.saveStepData(shpc002oid, sth_cur_step, docno, step_place, pics, remark, orgid);
    }

    /*@Override
    public int dothconfirm(String shpc002oid, String thno, String confirmplace, String pics, String remark, String orgid) throws Exception {
        return dao.dothconfirm(shpc002oid, thno, confirmplace, pics, remark, orgid);
    }*/

    @Override
    public int changeMiniprogflag(String miniprogflag, String orgid) throws Exception{
        return dao.changeMiniprogflag(miniprogflag, orgid);
    }

    /*@Override
    public List<Map<String, Object>> getBindedOpenidsByErpoid(String erpoid) {
        return dao.getBindedOpenidsByErpoid(erpoid);
    }*/

    @Override
    public boolean isNeedShconfirmMsg(String erpuseroid) {
        return dao.isNeedShconfirmMsg(erpuseroid);
    }

    @Override
    public boolean isOrguserExist(Map<String, Object> params) {
        return dao.isOrguserExist(params);
    }

    @Override
    public String addOrguserAccQywx(Map<String, Object> params) throws Exception {
        return dao.addOrguserAccQywx(params);
    }
}
