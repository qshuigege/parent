package com.fs.everything.repository.impl;

import com.fs.everything.repository.UCML_UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("ucml_UserDaoImpl")
public class UCML_UserDaoImpl implements UCML_UserDao {
    private static Logger log = LoggerFactory.getLogger(UCML_UserDao.class);

    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate mysqlJdbcTemplate;

    @Autowired
    @Qualifier("defaultJdbcTemplate")
    private JdbcTemplate sqlserverJdbcTemplate;

    /**
     *
     * @param params key:con_email_addr
     * @return key:personname,fsid,con_email_addr,orgname,ucml_useroid
     * @throws Exception
     */
    @Override
    public Map<String, Object> getUcmluserinfoByEmail(Map<String, Object> params) throws Exception{
        String sql = "select personname,fsid,con_email_addr,orgname,ucml_useroid from v_ucml_userinfo where con_email_addr=?";
        try {
            Map<String, Object> map = sqlserverJdbcTemplate.queryForMap(sql, new Object[]{params.get("con_email_addr")});
            return map;
        }catch (Exception e){
            log.error("UCML_UserDaoImpl.getUcmluserinfoByEmail() exception!-->{}", e.getMessage());
            throw new Exception("UCML_UserDaoImpl.getUcmluserinfoByEmail() exception!-->"+e.getMessage());
        }
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
            log.error("UCML_UserDaoImpl.getUcmluserinfoByFsid() exception!-->{}", e.getMessage());
            throw new Exception("UCML_UserDaoImpl.getUcmluserinfoByFsid() exception!-->"+e.getMessage());
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
            log.error("UCML_UserDaoImpl.getUcmlAllUserinfo() exception!-->{}", e.getMessage());
            throw new Exception("UCML_UserDaoImpl.getUcmlAllUserinfo() exception!-->"+e.getMessage());
        }
    }


}
