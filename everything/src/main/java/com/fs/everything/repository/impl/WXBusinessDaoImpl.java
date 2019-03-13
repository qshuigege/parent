package com.fs.everything.repository.impl;

import com.fs.everything.pojo.B2B_USER;
import com.fs.everything.pojo.WXAccount;
import com.fs.everything.repository.WXBusinessDao;
import com.fs.everything.utils.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class WXBusinessDaoImpl implements WXBusinessDao {
    private static Logger log = LoggerFactory.getLogger(WXBusinessDaoImpl.class);

    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("defaultJdbcTemplate")
    private JdbcTemplate sqlserverJdbcTemplate;

    @Override
    public boolean isBinded(String unionid, String openid) {
        String sql = "select count(0) num from wxaccount a, wxacc_orguser_relation r where a.unionid = r.unionid and a.unionid=?";
        //String sql = "select count(0) num from wxaccount a, wxacc_orguser_relation r where a.openid = r.openid and r.openid=? and r.appid=?";
        //Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[]{openid, R_WX.ORIGINAL_ID});
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[]{unionid});
        if(map.get("num")!=null&&"0".equals(map.get("num").toString())){
            return false;
        }else {
            return true;
        }
    }

    /*@Override
    public List<B2B_USER> getOrguserByOpenid(String unionid, String openid) throws Exception {
        //String sql = "select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE, FSKHBM as f_FSKHBM, miniprogflag FROM orguser WHERE ID =(select orgid from wxacc_orguser_relation where openid = ?)";
        String sql = "select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE, FSKHBM as f_FSKHBM, miniprogflag FROM orguser WHERE ID =(select orgid from wxacc_orguser_relation where unionid = ?)";
            List<B2B_USER> list = jdbcTemplate.query(sql, new Object[]{unionid}, new BeanPropertyRowMapper(B2B_USER.class));
            if (null==list||list.size()<1){
                throw new Exception("WXBusinessDaoImpl.getOrguserByUnionid() exception.根据openid在orguser表中查不到对应的数据！");
            }
            return list;
    }*/

    @Override
    public List<B2B_USER> getOrguserByUnionid(Map<String, Object> params) throws Exception {
        //String sql = "select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE, FSKHBM as f_FSKHBM FROM orguser WHERE ID =(select orgid from wxacc_orguser_relation where openid = ?)";
        String sql = "select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE, FSKHBM as f_FSKHBM FROM orguser WHERE ID =(select orgid from wxacc_orguser_relation where unionid = ?)";
        ///String sql = " select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE from orguser where USERID = ? and PWD = ?  ";
        //List<B2B_USER> list = null;

        try {
            List<B2B_USER> list = jdbcTemplate.query(sql, new Object[]{params.get("unionid")}, new BeanPropertyRowMapper(B2B_USER.class));
            if (null==list||list.size()<1){
                throw new Exception("WXBusinessDaoImpl.getOrguserByUnionid() exception.根据unionid在orguser表中查不到对应的数据，该微信号未与b2b账号绑定！");
            }
            return list;
        }catch (Exception e){
            throw new Exception("WXBusinessDaoImpl.getOrguserByUnionid() exception.根据unionid在orguser表中查不到对应的数据，该微信号未与b2b账号绑定！-->"+e.getMessage());
        }
    }

    @Override
    public List<B2B_USER> getOrguserByOrgid(Map<String, Object> params) throws Exception {
        String sql = "select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE, FSKHBM as f_FSKHBM FROM orguser WHERE ID =?";
        ///String sql = " select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE from orguser where USERID = ? and PWD = ?  ";
        //List<B2B_USER> list = null;

        try {
            List<B2B_USER> list = jdbcTemplate.query(sql, new Object[]{params.get("orgid")}, new BeanPropertyRowMapper(B2B_USER.class));
            if (null==list||list.size()<1){
                throw new Exception("WXBusinessDaoImpl.getOrguserByOrgid() exception.根据orgid在orguser表中查不到对应的数据！");
            }
            return list;
        }catch (Exception e){
            throw new Exception("WXBusinessDaoImpl.getOrguserByOrgid() exception.根据orgid在orguser表中查不到对应的数据！-->"+e.getMessage());
        }
    }

    @Override
    public List<B2B_USER> getOrguserByUserid(Map<String, Object> params) throws Exception {
        String sql = "select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE, FSKHBM as f_FSKHBM FROM orguser WHERE userid =?";
        ///String sql = " select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE from orguser where USERID = ? and PWD = ?  ";
        //List<B2B_USER> list = null;

        try {
            List<B2B_USER> list = jdbcTemplate.query(sql, new Object[]{params.get("userid")}, new BeanPropertyRowMapper(B2B_USER.class));
            if (null==list||list.size()<1){
                throw new Exception("WXBusinessDaoImpl.getOrguserByUserid() exception.根据userid在orguser表中查不到对应的数据！");
            }
            return list;
        }catch (Exception e){
            throw new Exception("WXBusinessDaoImpl.getOrguserByUserid() exception.根据userid在orguser表中查不到对应的数据！-->"+e.getMessage());
        }
    }

    @Override
    public int addMiniprogAcc(WXAccount acc) throws Exception {
        //if (!isAccExist(acc.getOpenid())) {
            String sql = "insert into wxaccount (openid, unionid, createdate, appid, nickname, sex, city, province, country, headimgurl) values(?,?,now(),?,?,?,?,?,?,?)";
            try {
                return jdbcTemplate.update(sql, new Object[]{acc.getOpenid(), acc.getUnionid(), acc.getAppid(), acc.getNickname(), acc.getSex(), acc.getCity(), acc.getProvince(), acc.getCountry(), acc.getHeadimgurl()});
            }catch (Exception e){
                throw new Exception("WXBusinessDaoImpl.addMiniprogAcc() exception!-->"+e.getMessage());
            }
        //}
        //return 0;
    }

    @Override
    public boolean isAccExist(String openid){
        String sql = "select count(0) num from wxaccount where openid=?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[]{openid});
        if(map.get("num")!=null&&"0".equals(map.get("num").toString())){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public Map<String, Object> getPSAddr(String psno) throws Exception {
        String sql = "select aaa.shpc002oid,aaa.address,aaa.fsno,aaa.sumcartons,aaa.basb001_fk_cust,bbb.partnername from shpc002 aaa,basb001 bbb  where aaa.BASB001_FK_Cust = bbb.BASB001OID and aaa.docno = ?";
        try {
            return sqlserverJdbcTemplate.queryForMap(sql, new Object[]{psno});
        }catch (Exception e){
            throw new Exception("WXBusinessDaoImpl.getPSAddr() exception! 根据该ps单号("+psno+")查不到数据！-->"+e.getMessage());
        }
    }

    @Override
    @Transactional(value="defaultTransactionManager",rollbackFor=Exception.class)
    public int doshconfirm(String shpc002oid, String psno, String confirmplace, String pics, String remark) throws Exception{
        Date date = new Date();
        StringBuffer sql = new StringBuffer("update shpc002 set signdate=?, sth_confirm_place=?, sth_remark=?, sth_pics=? where shpc002oid=? and docno=?");
        String sql2 = "update SHPE004D1 set signdate = ? where shpc002_fk = ?";
        //StringBuffer sql = new StringBuffer("update shpc002 set signdate=getdate(), sth_confirm_place=?, sth_remark=?, sth_pics=? where docno=?");
        int rows = sqlserverJdbcTemplate.update(sql.toString(), new Object[]{date, confirmplace, remark, pics, shpc002oid, psno});
        int rows2= sqlserverJdbcTemplate.update(sql2, new Object[]{date, shpc002oid});
        if (rows<1){
            throw new Exception("WXBusinessDaoImpl.doshconfirm() exception! shpc002oid或psno不存在！");
        }
        //sql.setLength(0);
        //String[] split = pics.split(",");
        //sql.append("insert into shpc002pics (SHPC002PICSOID,docno, picid,createdate) values(newid(),?,?,getdate())");
        /*sqlserverJdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, psno);
                ps.setString(2, split[i]);
            }

            @Override
            public int getBatchSize() {
                return split.length;
            }
        });*/
        //sqlserverJdbcTemplate.update(sql.toString(), new Object[]{psno, pics});
        return rows;
    }

    /*@Override
    public int dothconfirm(String shpc002oid, String thno, String confirmplace, String pics, String remark, String orgid) throws Exception {
        StringBuffer sql = new StringBuffer("update shpc002 set signdate=getdate(), sth_confirm_place=?, sth_remark=?, sth_pics=?, sth_orgid=? where shpc002oid=? and docno=?");
        int rows = sqlserverJdbcTemplate.update(sql.toString(), new Object[]{confirmplace, remark, pics, orgid, shpc002oid, thno});
        if (rows<1){
            throw new Exception("WXBusinessDaoImpl.dothconfirm() exception! thno不存在！");
        }
        return rows;
    }*/

    @Override
    public int changeMiniprogflag(String miniprogflag, String orgid) throws Exception {
        String sql = "update orguser set miniprogflag=? where id = ?";
         int rows = jdbcTemplate.update(sql, new Object[]{miniprogflag, orgid});
         if (rows==1){
             return rows;
         }else {
             throw new Exception("WXBusinessDaoImpl.changeMiniprogflag() exception! -->没有更新miniprogflag!");
         }
    }

    /*@Override
    public List<Map<String, Object>> getBindedOpenidsByErpoid(String erpoid) {
        //String sql = "select a.openid from (select r.unionid from wxacc_orguser_relation r join orguser o on o.id = r.orgid where o.erp_useroid=?) t join wxaccount a on t.unionid = a.unionid";
        String sql = "select r.openid from wxacc_orguser_relation r join orguser o on o.id = r.orgid where o.erp_useroid=? and r.appid = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{erpoid, R_WX.ORIGINAL_ID_MINIPROG});
    }*/

    @Override
    public boolean isNeedShconfirmMsg(String erpuseroid) {
        String sql = "select issignmage from basb001 where basb001oid=?";
        Map<String, Object> map = sqlserverJdbcTemplate.queryForMap(sql, new Object[]{erpuseroid});
        if(map.get("issignmage")!=null&&"1".equals(map.get("issignmage").toString())){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean isOrguserExist(Map<String, Object> params) {
        String sql = "select count(0) num from orguser where userid=?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[]{params.get("userid")});
        if(map.get("num")!=null&&"0".equals(map.get("num").toString())){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public String addOrguserAccQywx(Map<String, Object> params) throws Exception {

        String orgid = UUID.randomUUID().toString();
        String pwdmd5 = MD5Utils.encryptMD5("1234567");
        String sql = "insert into orguser(ID,createdate,USERID,USERNAME,PASSWORD,pwd,rolename,lastlogintime,role,ismanager,qywxuserid) values(?,now(),?,?,?,?,?,now(),?,'1',?)";
        try {
            int rows = jdbcTemplate.update(sql, new Object[]{orgid, params.get("userid"), params.get("name"), pwdmd5, "1234567", params.get("rolename"), params.get("role"), params.get("qywxuserid")});
            log.info("WXBusinessDaoImpl.addOrguserAccQywx() orgid-->{}, rows-->{}",orgid, rows);
        }catch (Exception e){
            log.error("WXBusinessDaoImpl.addOrguserAccQywx() exception!-->{}", e.getMessage());
            throw new Exception("WXBusinessDaoImpl.addOrguserAccQywx() exception!-->"+e.getMessage());
        }
        return orgid;
    }

    @Override
    public List<Map<String, Object>> getTHInfo(String thno) throws Exception {
        String sql = "select aaa.shpc002oid,aaa.docno,aaa.fsno,aaa.partnershortname,aaa.deliverycompany,aaa.tel,aaa.address,aaa.attn,aaa.yjthtime,aaa.sumcartons,aaa.sumboard,aaa.remark,bbb.signdate,bbb.step from shpc002 aaa, shpc002pics bbb where aaa.docno=bbb.docno and aaa.docno=? order by bbb.step";
        List<Map<String, Object>> lst = sqlserverJdbcTemplate.queryForList(sql, new Object[]{thno});
        if (lst.size()<1) {
            sql = "select shpc002oid,docno,fsno,deliverycompany,tel,address,attn,yjthtime,sumcartons,sumboard,remark, null as signdate, '0' as step from shpc002 where docno=?";
            lst = sqlserverJdbcTemplate.queryForList(sql, new Object[]{thno});
        }
        if (lst.size()<1){
            throw new Exception("WXBusinessDaoImpl.getTHInfo() exception! 根据该th单号("+thno+")查不到数据");
        }
        return lst;
    }

    @Override
    @Transactional(value = "defaultTransactionManager",rollbackFor=Exception.class)
    public int saveStepData(String shpc002oid, String sth_cur_step, String docno, String step_place, String pics, String remark, String orgid) throws Exception {
        String sql = "insert into shpc002pics (SHPC002PICSOID,signdate,docno, step,pics,addr,remark,orgid,shpc002_fk) values(newid(),getdate(),?,?,?,?,?,?,?)";
        int rows = sqlserverJdbcTemplate.update(sql, new Object[]{docno, sth_cur_step, pics, step_place, remark, orgid,shpc002oid});
        if ("3".equals(sth_cur_step)){
            sql = "update shpc002 set signdate=getdate(), sth_confirm_place=?, sth_remark=?, sth_pics=?, sth_orgid=? where shpc002oid=? and docno=?";
            sqlserverJdbcTemplate.update(sql, new Object[]{step_place, remark, pics, orgid, shpc002oid, docno});
        }
        return 1;
    }

    /*@Override
    public int saveStepData(String sth_cur_step, String docno) throws Exception {
        StringBuffer sql = new StringBuffer("update shpc002 set ");
        if ("1".equals(sth_cur_step)){
            sql.append("sth_time_step1");
        }else if("2".equals(sth_cur_step)){
            sql.append("sth_time_step2");
        }else if ("3".equals(sth_cur_step)){
            sql.append("sth_time_step3");
        }else if ("4".equals(sth_cur_step)){
            sql.append("sth_time_step4");
        }else {
            throw new Exception("WXBusinessDaoImpl.setStepTime() exception! 参数sth_cur_step的值只能为1,2,3,4！");
        }
        sql.append(" =getdate(),sth_cur_step=? where docno=? ");

        return sqlserverJdbcTemplate.update(sql.toString(), new Object[]{sth_cur_step, docno});
    }*/


}
