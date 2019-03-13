package com.fs.something.repository.impl;

import com.fs.something.pojo.B2B_USER;
import com.fs.something.repository.UserDao;
import com.fs.something.utils.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class UserDaoImpl implements UserDao {
    private static Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("defaultJdbcTemplate")
    private JdbcTemplate sqlserverJdbcTemplate;

    @Override
    public boolean isOrguserExistByUserid(Map<String, Object> params) {
        String sql = "select count(0) num from t_qywx_user where userid=?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[]{params.get("userid")});
        if(map.get("num")!=null&&"0".equals(map.get("num").toString())){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public boolean isOrguserExistByQywxuserid(Map<String, Object> params) {
        String sql = "select count(0) num from t_qywx_user where qywxuserid=?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[]{params.get("qywxuserid")});
        if(map.get("num")!=null&&"0".equals(map.get("num").toString())){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public String addOrguserAccQywx(Map<String, Object> params) throws Exception {

        String sql = "select top 1 ucml_useroid, fsid, orgname from v_UCML_UserInfo where con_email_addr = ?";
        Map<String, Object> empnumMap = null;
        try {
            empnumMap = sqlserverJdbcTemplate.queryForMap(sql, new Object[]{params.get("userid")});
        }catch (Exception e){
            throw new Exception("WXBusinessDaoImpl.addOrguserAccQywx() exception! 根据con_email_addr("+params.get("userid")+")在视图v_UCML_UserInfo中查不到数据！");
        }
        String orgid = UUID.randomUUID().toString();
        String pwdmd5 = MD5Utils.encryptMD5("1234567");
        sql = "insert into t_qywx_user(ID,createdate,USERID,USERNAME,PASSWORD,pwd,rolename,lastlogintime,role,ismanager,ucml_useroid,empnum,ucml_department_name,qywxuserid,qywxnickname,qywxavatar) values(?,now(),?,?,?,?,?,now(),?,'1',?,?,?,?,?,?)";
        try {
            int rows = jdbcTemplate.update(sql, new Object[]{orgid, params.get("userid"), params.get("name"), pwdmd5, "1234567", params.get("rolename"), params.get("role"), empnumMap.get("ucml_useroid"), empnumMap.get("fsid"), empnumMap.get("orgname"), params.get("qywxuserid"), params.get("qywxnickname"), params.get("qywxavatar")});
            log.info("WXBusinessDaoImpl.addOrguserAccQywx() orgid-->{}, rows-->{}",orgid, rows);
        }catch (Exception e){
            log.error("WXBusinessDaoImpl.addOrguserAccQywx() exception!-->{}", e.getMessage());
            throw new Exception("WXBusinessDaoImpl.addOrguserAccQywx() exception!-->"+e.getMessage());
        }
        return orgid;
    }

    /**
     *
     * @param params orgid
     * @return
     * @throws Exception
     */
    @Override
    public List<B2B_USER> getOrguserByOrgid(Map<String, Object> params) throws Exception {
        String sql = "select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE, FSKHBM as f_FSKHBM, qywxuserid, qywxnickname, qywxavataar,userid as loginid, empnum, ucml_useroid, ucml_department_name FROM t_qywx_user WHERE ID =?";
        ///String sql = " select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE from t_qywx_user where USERID = ? and PWD = ?  ";
        //List<B2B_USER> list = null;

        try {
            List<B2B_USER> list = jdbcTemplate.query(sql, new Object[]{params.get("orgid")}, new BeanPropertyRowMapper(B2B_USER.class));
            if (null==list||list.size()<1){
                throw new Exception("UserDaoImpl.getOrguserByOrgid() exception.根据orgid在orguser表中查不到对应的数据！");
            }
            return list;
        }catch (Exception e){
            throw new Exception("UserDaoImpl.getOrguserByOrgid() exception.根据orgid在orguser表中查不到对应的数据！-->"+e.getMessage());
        }
    }

    /**
     *
     * @param params userid
     * @return
     * @throws Exception
     */
    @Override
    public List<B2B_USER> getOrguserByUserid(Map<String, Object> params) throws Exception {
        String sql = "select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE, FSKHBM as f_FSKHBM, qywxuserid, qywxnickname, qywxavatar,userid as loginid, empnum, ucml_useroid, ucml_department_name FROM t_qywx_user WHERE userid =?";
        ///String sql = " select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE from t_qywx_user where USERID = ? and PWD = ?  ";
        //List<B2B_USER> list = null;

        List<B2B_USER> list = jdbcTemplate.query(sql, new Object[]{params.get("userid")}, new BeanPropertyRowMapper(B2B_USER.class));
        if (null==list||list.size()<1){
            throw new Exception("UserDaoImpl.getOrguserByUserid() exception.根据userid在orguser表中查不到对应的数据！");
        }
        return list;
    }

    /**
     *
     * @param params UserId
     * @return
     * @throws Exception
     */
    @Override
    public List<B2B_USER> getOrguserByQywxuserid(Map<String, Object> params) throws Exception {
        String sql = "select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE, FSKHBM as f_FSKHBM, qywxuserid, qywxnickname, qywxavatar,userid as loginid, empnum, ucml_useroid, ucml_department_name FROM t_qywx_user WHERE qywxuserid =?";
        ///String sql = " select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE from t_qywx_user where USERID = ? and PWD = ?  ";
        //List<B2B_USER> list = null;

        List<B2B_USER> list = jdbcTemplate.query(sql, new Object[]{params.get("UserId")}, new BeanPropertyRowMapper(B2B_USER.class));
        if (null==list||list.size()<1){
            throw new Exception("UserDaoImpl.getOrguserByQywxuserid() exception.根据qywxuserid在orguser表中查不到对应的数据！");
        }
        return list;
    }

    @Override
    public List<B2B_USER> getOrguserByUcmluseroid(Map<String, Object> params) throws Exception {
        String sql = "select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE, FSKHBM as f_FSKHBM, qywxuserid, qywxnickname, qywxavatar,userid as loginid, empnum, ucml_useroid, ucml_department_name FROM t_qywx_user WHERE ucml_useroid =?";
        ///String sql = " select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE from t_qywx_user where USERID = ? and PWD = ?  ";
        //List<B2B_USER> list = null;

        List<B2B_USER> list = jdbcTemplate.query(sql, new Object[]{params.get("ucml_useroid")}, new BeanPropertyRowMapper(B2B_USER.class));
        if (null==list||list.size()<1){
            throw new Exception("UserDaoImpl.getOrguserByUcmluseroid() exception.根据ucml_useroid在orguser表中查不到对应的数据！");
        }
        return list;
    }

    @Override
    public List<B2B_USER> getOrguserByFsid(Map<String, Object> params) throws Exception {
        String sql = "select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE, FSKHBM as f_FSKHBM, qywxuserid, qywxnickname, qywxavatar,userid as loginid, empnum, ucml_useroid, ucml_department_name FROM t_qywx_user WHERE empnum = ?";
        ///String sql = " select ID as f_ID,ERP_USEROID as f_KH_FK,ISMANAGER as f_YHLX,USERNAME as f_YHMC,ROLE as f_ROLE from t_qywx_user where USERID = ? and PWD = ?  ";
        //List<B2B_USER> list = null;

        List<B2B_USER> list = jdbcTemplate.query(sql, new Object[]{params.get("empnum")}, new BeanPropertyRowMapper(B2B_USER.class));
        if (null==list||list.size()<1){
            throw new Exception("UserDaoImpl.getOrguserByFsid() exception.根据empnum在orguser表中查不到对应的数据！");
        }
        return list;
    }

    @Override
    public Map<String, Object> getUcmluserinfoByUcmluseroid(Map<String, Object> params) throws Exception {
        String sql = "select personname,fsid,con_email_addr,orgname,ucml_useroid from v_ucml_userinfo where ucml_useroid=?";
        try {
            Map<String, Object> map = sqlserverJdbcTemplate.queryForMap(sql, new Object[]{params.get("ucml_useroid")});
            return map;
        }catch (Exception e){
            log.error("UserDaoImpl.getUcmluserinfoByUcmluseroid() exception!-->{}", e.getMessage());
            throw new Exception("UserDaoImpl.getUcmluserinfoByUcmluseroid() exception!-->"+e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getUserZrzx(Map<String, Object> params) throws Exception{
        String sql = "SELECT top 1 orgname FROM V_VIEW_FYBXLOGIC WHERE con_emp_num= ?";
        return sqlserverJdbcTemplate.queryForMap(sql, new Object[]{params.get("empnum")});
    }

    @Override
    public List<Map<String, Object>> getLeaderAuditInfo(Map<String, Object> params){
        String sql = "select processcode, processnodes from process_audit_leader_info where empnum = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{params.get("empnum")});
    }


    /**
     *
     * @param params key:fsid
     * @return key:personname,fsid,con_email_addr,orgname,ucml_useroid
     * @throws Exception
     */
    @Override
    public Map<String, Object> getUcmluserinfoByFsid(Map<String, Object> params) throws Exception{
        String sql = "select top 1 bbb.orgname zrzx,bbb.varchar6,aaa.personname,aaa.fsid,aaa.con_email_addr,aaa.orgname,aaa.ucml_useroid from v_ucml_userinfo aaa left join V_VIEW_FYBXLOGIC bbb on aaa.fsid=bbb.con_emp_num where fsid=?";
        try {
            Map<String, Object> map = sqlserverJdbcTemplate.queryForMap(sql, new Object[]{params.get("fsid")});
            return map;
        }catch (Exception e){
            log.error("UserDaoImpl.getUcmluserinfoByFsid() exception!-->{}", e.getMessage());
            throw new Exception("UserDaoImpl.getUcmluserinfoByFsid() exception!-->"+e.getMessage());
        }
    }


    /**
     *
     * @return key:personname,fsid,con_email_addr,orgname,ucml_useroid
     * @throws Exception
     */
    @Override
    public List<Map<String, Object>> getUcmlAllUserinfo() throws Exception{
        String sql = "select personname,fsid,con_email_addr,orgname,ucml_useroid from v_ucml_userinfo";
        try {
            List<Map<String, Object>> list = sqlserverJdbcTemplate.queryForList(sql);
            return list;
        }catch (Exception e){
            log.error("UserDaoImpl.getUcmlAllUserinfo() exception!-->{}", e.getMessage());
            throw new Exception("UserDaoImpl.getUcmlAllUserinfo() exception!-->"+e.getMessage());
        }
    }

}
