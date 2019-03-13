package com.fs.nothing.repository.impl;

import com.fs.nothing.pojo.B2B_USER;
import com.fs.nothing.repository.WXBusinessDao;
import com.fs.nothing.wx.R_WX;
import com.fs.nothing.wx.apimodel.WXAccount;
import com.fs.nothing.wx.apimodel.WXUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void addWXAcc(WXAccount acc) {
        String sql = "insert into wxaccount(subscribe, openid, nickname, sex, language, city, province, country, headimgurl, subscribe_time, remark, groupid, tagid_list, subscribe_scene, qr_scene, qr_scene_str, unionid, createdate, appid) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            int rows = jdbcTemplate.update(sql, new Object[]{acc.getSubscribe(), acc.getOpenid(), acc.getNickname(), acc.getSex(), acc.getLanguage(), acc.getCity(), acc.getProvince(), acc.getCountry(), acc.getHeadimgurl(), acc.getSubscribe_time(), acc.getRemark(), acc.getGroupid(), acc.getTagid_list(), acc.getSubscribe_scene(), acc.getQr_scene(), acc.getQr_scene_str(), acc.getUnionid(), acc.getCreatedate(), acc.getAppid()});
        }catch (Exception e){
            //e.printStackTrace();
            log.info("WXBusinessDaoImpl.addWXAcc() insert exception. -->{}", e.getMessage());

            sql = "update wxaccount t set t.unionid=?, t.nickname=?, t.sex=?, t.province=?, t.city=?, t.country=?, t.headimgurl=?, t.language=?, t.subscribe_time=?, t.remark=?, t.groupid=?, t.tagid_list=?, t.subscribe_scene=?, t.qr_scene=?, t.qr_scene_str=? where t.openid=?";
            try {
                jdbcTemplate.update(sql, new Object[]{acc.getUnionid(), acc.getNickname(), acc.getSex(), acc.getProvince(), acc.getCity(), acc.getCountry(), acc.getHeadimgurl(), acc.getLanguage(), acc.getSubscribe_time(), acc.getRemark(), acc.getGroupid(), acc.getTagid_list(), acc.getSubscribe_scene(), acc.getQr_scene(), acc.getQr_scene_str(),acc.getOpenid()});
            }catch (Exception e2){
                e2.printStackTrace();
                log.error("WXBusinessDaoImpl.addWXAcc() update exception. -->{}", e2.getMessage());
            }
        }
    }

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

    @Override
    public boolean binding(String unionid, String orgid, String openid) {
        String sql = "insert into wxacc_orguser_relation (uuid,unionid,orgid,createdate,openid,appid) values(UUID(),?,?,sysdate(),?,?)";
        int rows = jdbcTemplate.update(sql, new Object[]{unionid, orgid, openid,R_WX.ORIGINAL_ID});
        if (rows>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public String getOrgidByUniqueid(String uniqueid) throws Exception{
        String sql = "select id from orguser where uniqueid = ?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[]{uniqueid});
        if(null == map.get("id")){
            throw new Exception("WXBusinessDaoImpl.getOrgidByUniqueid() 根据二维码的参数uniqueid在orguser表中查询不到orgid");
        }else {
            return map.get("id").toString();
        }
    }

    /*@Override
    public boolean unbinding(String unionid, String openid) {
        //String sql = "delete from wxacc_orguser_relation where unionid = ? and openid = ?";
        String sql = "delete from wxacc_orguser_relation where unionid = ?";
        int rows = jdbcTemplate.update(sql, new Object[]{unionid});
        if (rows>0){
            return true;
        }else {
            return false;
        }
    }*/

    @Override
    public boolean unbinding(Map<String, Object> params) {
        //String sql = "delete from wxacc_orguser_relation where unionid = ? and openid = ?";
        String sql = "delete from wxacc_orguser_relation where unionid = ?";
        int rows = jdbcTemplate.update(sql, new Object[]{params.get("unionid")});
        if (rows>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public int unsubscribe(String openid) {
        String sql = "update wxaccount set SUBSCRIBE = '0' where OPENID = ?";
        return jdbcTemplate.update(sql, new Object[]{openid});
    }



    @Override
    public Map<String, Object> getErpuseroidByOpenid(String openid) throws Exception{
        String sql = "select erp_useroid FROM orguser WHERE ID =(SELECT r.orgid from WXACCOUNT a join WXACC_ORGUSER_RELATION r on a.unionid = r.unionid where a.openid=?)";
        try {
            Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[]{openid});
            return map;
        }catch (Exception e){
            throw new Exception("WXBusinessDaoImpl.getErpuseroidByOpenid() exception.根据openid在orguser表中查不到对应的eru_useroid，该微信号未与b2b账号绑定！-->"+e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getUnionidByOpenid(String openid) throws Exception {
        String sql = "select unionid from wxaccount where openid=?";
        try {
            return jdbcTemplate.queryForMap(sql, new Object[]{openid});
        }catch (Exception e){
            throw new Exception("WXBusinessDaoImpl.getUnionidByOpenid() exception. 根据openid在wxaccount表中查不到unionid-->"+e.getMessage());
        }
    }

    /*@Override
    public List<B2B_USER> getOrguserByOpenid(String unionid, String openid) throws Exception {
        //String sql = "select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE, FSKHBM as f_FSKHBM FROM orguser WHERE ID =(select orgid from wxacc_orguser_relation where openid = ?)";
        String sql = "select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE, FSKHBM as f_FSKHBM FROM orguser WHERE ID =(select orgid from wxacc_orguser_relation where unionid = ?)";
        ///String sql = " select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE from orguser where USERID = ? and PWD = ?  ";
        //List<B2B_USER> list = null;

        try {
            List<B2B_USER> list = jdbcTemplate.query(sql, new Object[]{unionid}, new BeanPropertyRowMapper(B2B_USER.class));
            if (null==list||list.size()<1){
                throw new Exception("WXBusinessDaoImpl.getOrguserByUnionid() exception.根据unionid在orguser表中查不到对应的数据，该微信号未与b2b账号绑定！");
            }
            return list;
        }catch (Exception e){
            throw new Exception("WXBusinessDaoImpl.getOrguserByUnionid() exception.根据unionid在orguser表中查不到对应的数据，该微信号未与b2b账号绑定！-->"+e.getMessage());
        }
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
    public int updateLastLoginTime(String orgid) throws Exception {
        String sql = "update orguser set lastlogintime = SYSDATE() where ID = ?";
        try {
            return jdbcTemplate.update(sql, new Object[]{orgid});
        }catch (Exception e){
            throw new Exception("WXBusinessDaoImpl.updateLastLoginTime() exception.-->"+e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getBindedOpenidsByErpoid(String erpoid) {
        //String sql = "select a.openid from (select r.unionid from wxacc_orguser_relation r join orguser o on o.id = r.orgid where o.erp_useroid=?) t join wxaccount a on t.unionid = a.unionid";
        //String sql = "select r.openid from wxacc_orguser_relation r join orguser o on o.id = r.orgid where o.erp_useroid=? and r.appid = ?";
        String sql = "select r.openid from wxacc_orguser_relation r join orguser o on o.id = r.orgid where o.erp_useroid=?";
        return jdbcTemplate.queryForList(sql, new Object[]{erpoid});
    }

    @Override
    public List<Map<String, Object>> getBindedWXAccs(String orgid) throws Exception {
        //String sql = "select aaa.* from (select a.openid, a.unionid, a.headimgurl, a.nickname, t.createdate from wxaccount a right join (select r.unionid, r.createdate from wxacc_orguser_relation r where r.orgid = ?)t on a.unionid = t.unionid) aaa group by aaa.unionid order by aaa.createdate desc";
        //String sql = "select a.openid, a.unionid, a.headimgurl, a.nickname, t.createdate from wxaccount a join (select r.openid, r.createdate from wxacc_orguser_relation r where r.orgid = ? and r.appid = ?)t on a.openid = t.openid order by a.createdate desc";
        String sql = "select a.openid, a.unionid, a.headimgurl, a.nickname, t.createdate from wxaccount a join (select r.openid, r.createdate from wxacc_orguser_relation r where r.orgid = ?)t on a.openid = t.openid order by t.createdate desc";
        return jdbcTemplate.queryForList(sql, new Object[]{orgid});
    }

    @Override
    public int unbindWXAccsBatch(String[] unionids) throws Exception {
        StringBuffer sql = new StringBuffer("delete from wxacc_orguser_relation where unionid in (");
        for (int i = 0; i < unionids.length; i++) {
            sql.append("'").append(unionids[i]).append("',");
        }
        sql.setLength(sql.length()-1);
        sql.append(") and appid='");
        sql.append(R_WX.ORIGINAL_ID);
        sql.append("'");
        return jdbcTemplate.update(sql.toString());
    }

    @Override
    public int[] updateHeadImg(List<WXUserInfo> userList) throws Exception {

        String sql = "update wxaccount set HEADIMGURL = ?, NICKNAME = ? where openid = ?";
        int[] retval = null;
        try {
            retval = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    WXUserInfo u = userList.get(i);
                    ps.setString(1, u.getHeadimgurl());
                    ps.setString(2, u.getNickname());
                    ps.setString(3, u.getOpenid());
                }

                @Override
                public int getBatchSize() {
                    return userList.size();
                }
            });
        }catch (Exception e){
            throw new Exception("WXBusinessDaoImpl.updateHeadImg() exception. -->"+e.getMessage());
        }
        return retval;
    }

    @Override
    public List<Map<String, Object>> getConditionOrder(String erpuseroid, String[] orderStatus, String fsno, String custOrderNo,String pageSize, String rowIndex)throws Exception{
        StringBuffer sql = new StringBuffer("");
        /*select O1.orda001oid,O1.FSNO,O1.OrderStatus,O1.CustOrderNo from b2b_ordSelList O1
                where
		 <if test="ERPUSERID != null and ERPUSERID != '' ">
                O1.BASB001_FK= #{ERPUSERID}
    	 </if>
    	 <if test="ORDERSTATUS != null and ORDERSTATUS.length !=0 ">
    	 	<!-- and O1.OrderStatus in (#{ORDERSTATUS})  -->
                and O1.OrderStatus in
    	 	<foreach item="item" index="index" collection="ORDERSTATUS" open="(" separator="," close=")">
                        #{item}
             </foreach>
    	 </if>
    	 <if test="FSNO != null and FSNO != '' ">
                and O1.FSNO LIKE CONCAT('%','${FSNO}','%' )
                </if>
    	 <if test="CUSTORDERNO != null and CUSTORDERNO != '' ">
                and O1.CUSTORDERNO  LIKE CONCAT('%','${CUSTORDERNO}','%' )
                </if>
        order by O1.OrderDate desc*/

        if(null==erpuseroid||"".equals(erpuseroid)){
            throw new Exception("WXBusinessDaoImpl.getConditionOrder() exception!-->参数erpuseroid为空！");
        }
        List<Object> paramList = new ArrayList<>();
        //sql.append("select top ").append(pageSize).append(" orda001oid,FSNO,OrderStatus,CustOrderNo,OrderDate from (SELECT  ROW_NUMBER() OVER (order by ttt.ORDERDATE DESC) as rownum, * FROM(");
        sql.append("select top ").append(pageSize).append(" orda001oid,FSNO,OrderStatus,OrderDate from (SELECT  ROW_NUMBER() OVER (order by ttt.ORDERDATE DESC) as rownum, * FROM(");
        //sql.append(" select O1.orda001oid,O1.FSNO,O1.OrderStatus,O1.CustOrderNo,O1.OrderDate from b2b_ordSelList O1 where O1.BASB001_FK= ? ");
        sql.append(" select O1.orda001oid,O1.FSNO,O1.OrderStatus,O1.OrderDate from b2b_ordSelListNew O1 where O1.BASB001_FK= ? ");
        paramList.add(erpuseroid);

        if(orderStatus!=null&&orderStatus.length>0){
            sql.append(" and O1.OrderStatus in (");
            for (int i = 0; i < orderStatus.length; i++) {
                sql.append("?,");
                paramList.add(orderStatus[i]);
            }
            sql.setLength(sql.length()-1);
            sql.append(") ");
        }
        if(fsno!=null&&!"".equals(fsno)){
            //sql.append(" and (O1.FSNO LIKE CONCAT('%',?,'%' ) or O1.CUSTORDERNO LIKE CONCAT('%',?,'%' )) ");
            sql.append(" and O1.FSNO LIKE CONCAT('%',?,'%') ");
            paramList.add(fsno);
            //paramList.add(fsno);
        }
        /*if (null!=custOrderNo&&!"".equals(custOrderNo)){
            sql.append(" and O1.CUSTORDERNO  LIKE CONCAT('%',?,'%' ) ");
            paramList.add(custOrderNo);
        }*/
        sql.append(" )ttt)uuu where uuu.rownum>").append(rowIndex);
        //sql.append(" order by O1.OrderDate desc ");
        return sqlserverJdbcTemplate.queryForList(sql.toString(), paramList.toArray());
    }

    @Override
    public List<Map<String, Object>> getOrderAndLogistics(String orda001oid) throws Exception {
        /*select O1.orda001oid,O1.FSNO,O1.OrderStatus,O1.CustOrderNo from b2b_ordSelList O1
                where
		 <if test="ERPUSERID != null and ERPUSERID != '' ">
                O1.orda001oid= #{ERPUSEROID}
    	 </if>*/
        if(null==orda001oid||"".equals(orda001oid)){
            throw new Exception("WXBusinessDaoImpl.getOrderAndLogistics() exception!-->参数orda001oid为空！");
        }
        //String sql = "select O1.orda001oid,O1.FSNO,O1.OrderStatus,O1.CustOrderNo from b2b_ordSelList O1 where O1.orda001oid=?";
        String sql = "select O1.orda001oid,O1.FSNO,O1.OrderStatus from b2b_ordSelListNew O1 where O1.orda001oid=?";

        return sqlserverJdbcTemplate.queryForList(sql, new Object[]{orda001oid});
    }

    @Override
    public List<Map<String, Object>> getLogisticMobileWeb(String orda001oid) throws Exception {
        /*select  otime,otype,refno,Remark from ORDHistoryD1
            where
		 <if test="ERPUSEROID != null and ERPUSEROID != '' ">
        ORDHistory_FK= #{ERPUSEROID}
             </if>
        and ono not in (2,3,5,6,9) order by OTime*/
        if(null==orda001oid||"".equals(orda001oid)){
            throw new Exception("WXBusinessDaoImpl.getLogisticMobileWeb() exception!-->参数orda001oid为空！");
        }
        StringBuffer sql = new StringBuffer("select otime,otype,refno,Remark, '' kdno from ORDHistoryD1 where ORDHistory_FK=? and ono not in (2,3,5,6,9,10,11,12,80) order by OTime");
        List<Map<String, Object>> list1 = sqlserverJdbcTemplate.queryForList(sql.toString(), new Object[]{orda001oid});

        sql.setLength(0);
        sql.append("SELECT DISTINCT CASE h2.ono WHEN '80' THEN  s2.SignDate ELSE h2.otime END AS  otime,h2.otype,h2.refno,h2.Remark,s2.AWBNO kdno FROM SHPC002 s2 INNER JOIN ORDHistoryD1 h2 ON s2.Docno=h2.Refno WHERE h2.ORDHistory_FK=? and h2.ono IN (10,11,12,80) order by OTime");
        List<Map<String, Object>> list2 = sqlserverJdbcTemplate.queryForList(sql.toString(), new Object[]{orda001oid});

        List<Map<String, Object>> tempList = new ArrayList<>();
        Map<String, Map<String, Object>> yhq = new HashMap<>();
        for (int i = 0; i < list2.size(); i++) {
            Map<String, Object> map = list2.get(i);
            String remark = map.get("Remark")==null?"": map.get("Remark").toString();
            String[] split = remark.split(",");
            if (split.length<3){
                String kdno = map.get("kdno")==null?"": map.get("kdno").toString();
                map.put("Remark", remark+kdno);
            }
            if ("已回签".equals(map.get("otype").toString())){
                yhq.put(map.get("refno").toString(), map);
            }else {
                tempList.add(map);
            }
        }

        /*List<Map<String, Object>> tempList = new ArrayList<>();
        Map<String, Map<String, Object>> yhq = new HashMap<>();
        for (int i = 0; i < list2.size(); i++) {
            Map<String, Object> map = list2.get(i);
            if ("已回签".equals(map.get("otype").toString())){
                yhq.put(map.get("refno").toString(), map);
            }else {
                tempList.add(map);
            }
        }*/

        List<Map<String,Object>> dataList = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
            Map<String, Object> map = tempList.get(i);
            dataList.add(map);
            if(null!=yhq.get(map.get("refno").toString())){
                dataList.add(yhq.get(map.get("refno").toString()));
            }
        }



        for (int i = 0; i < dataList.size(); i++) {
            list1.add(dataList.get(i));
        }
        
        
        return list1;
    }


    /*@Override
    public List<Map<String, Object>> getLogisticMobileWeb(String orda001oid) throws Exception {
        *//*select  otime,otype,refno,Remark from ORDHistoryD1
            where
		 <if test="ERPUSEROID != null and ERPUSEROID != '' ">
        ORDHistory_FK= #{ERPUSEROID}
             </if>
        and ono not in (2,3,5,6,9) order by OTime*//*
        if(null==orda001oid||"".equals(orda001oid)){
            throw new Exception("WXBusinessDaoImpl.getLogisticMobileWeb() exception!-->参数orda001oid为空！");
        }
        String sql = "select otime,otype,refno,Remark from ORDHistoryD1 where ORDHistory_FK=? and ono not in (2,3,5,6,9,80) order by OTime";
        List<Map<String, Object>> list = sqlserverJdbcTemplate.queryForList(sql, new Object[]{orda001oid});
        Map<String, Map<String, Object>> tmpMap = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> m = list.get(i);
            String refno = m.get("refno").toString();
            if (refno.startsWith("PS")){
                tmpMap.put(refno, m);
                sb.append("'").append(refno).append("',");
            }
        }
        if (sb.length()>0){
            sb.setLength(sb.length()-1);
            StringBuffer sql2 = new StringBuffer();
            sql2.append("SELECT h2.refno,s2.AWBNO FROM SHPC002 s2 INNER JOIN ORDHistoryD1 h2 ON s2.Docno=h2.Refno  WHERE h2.Refno in(").append(sb).append(")");
            List<Map<String, Object>> kdnoList = sqlserverJdbcTemplate.queryForList(sql2.toString());
            Map<String, Map<String,Object>> tmpMap2 = new HashMap<>();
            for (int i = 0; i < kdnoList.size(); i++) {
                tmpMap2.put(kdnoList.get(i).get("refno").toString(), kdnoList.get(i));
            }

            Iterator<Map.Entry<String, Map<String, Object>>> iterator = tmpMap2.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, Map<String, Object>> entry= iterator.next();
                Map<String, Object> t1 = tmpMap.get(entry.getKey());
                Map<String, Object> t2 = entry.getValue();
                //String tmpStr = t1.get("Remark").toString();
                //t1.put("Remark", t1.get("Remark")==null?"":t1.get("Remark").toString()+","+t2.get("AWBNO")==null?"":t2.get("AWBNO"));
                String remark1 = t1.get("Remark")==null?"":t1.get("Remark").toString();
                String[] split = remark1.split(",");
                if (split.length>=3){
                    remark1 = split[0]+","+split[1];
                }
                String AWBNO = t2.get("AWBNO")==null?"":t2.get("AWBNO").toString();
                t1.put("Remark", remark1+","+AWBNO);
            }
            sql2.setLength(0);
            sql2.append("SELECT s2.SignDate otime,h2.otype,h2.refno,h2.Remark FROM SHPC002 s2 INNER JOIN ORDHistoryD1 h2 ON s2.Docno=h2.Refno  WHERE h2.ORDHistory_FK=? and h2.ono  in (80) GROUP BY  s2.SignDate,h2.otype,h2.refno,h2.Remark");
            List<Map<String, Object>> mapList = sqlserverJdbcTemplate.queryForList(sql2.toString(), new Object[]{orda001oid});
            for (int i = 0; i < mapList.size(); i++) {
                Map<String, Object> map = mapList.get(i);
                //map.put("Remark", ","+map.get("Remark").toString());
                list.add(map);
            }
        }
        return list;
    }*/

    @Override
    public List<Map<String, Object>> getOrderMobileWeb(String erpuseroid, String minono, String maxono,String pageSize, String rowIndex) throws Exception{
        StringBuffer sql = new StringBuffer("");
        /*select O1.orda001oid,O1.FSNO,O1.OrderStatus,O1.CustOrderNo from b2b_ordSelList O1
                where
		 <if test="ERPUSERID != null and ERPUSERID != '' ">
                O1.BASB001_FK= #{ERPUSERID}
    	 </if>
    	 <if test=" MINONO == 9 ">
                and convert(decimal(10,0),O1.ONO) &lt;  #{MINONO}
    	 </if>
    	  <if test=" MAXONO == 9 ">
                and convert(decimal(10,0),O1.ONO) &gt;= #{MAXONO}
    	 </if>
        order by O1.OrderDate desc*/

        if(null==erpuseroid||"".equals(erpuseroid)){
            throw new Exception("WXBusinessDaoImpl.getOrderMobileWeb() exception!-->参数erpuseroid为空！");
        }
        List<String> paramList = new ArrayList<>();
        //sql.append(" select top ").append(pageSize).append(" orda001oid,FSNO,OrderStatus,CustOrderNo,   from (SELECT  ROW_NUMBER() OVER (order by ttt.ORDERDATE DESC) as rownum, * FROM( ");
        sql.append(" select top ").append(pageSize).append(" orda001oid,FSNO,OrderStatus,OrderDate from (SELECT  ROW_NUMBER() OVER (order by ttt.ORDERDATE DESC) as rownum, * FROM( ");
        //paramList.add(pageSize);
        sql.append(" select O1.orda001oid,O1.FSNO,O1.OrderStatus,O1.OrderDate from b2b_ordSelListNew O1 where O1.BASB001_FK=? ");
        paramList.add(erpuseroid);

        if ("9".equals(minono)){
            sql.append(" and convert(decimal(10,0),O1.ONO) <  ? ");
            paramList.add(minono);
        }
        if("9".equals(maxono)){
            sql.append(" and convert(decimal(10,0),O1.ONO) >= ? ");
            paramList.add(maxono);
        }
        sql.append(" )ttt)uuu where uuu.rownum> ").append(rowIndex);
        //paramList.add(rowIndex);
        //sql.append(" order by O1.OrderDate desc ");

        return sqlserverJdbcTemplate.queryForList(sql.toString(),paramList.toArray());

    }
}
