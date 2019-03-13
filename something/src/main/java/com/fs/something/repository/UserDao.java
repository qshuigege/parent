package com.fs.something.repository;

import com.fs.something.pojo.B2B_USER;

import java.util.List;
import java.util.Map;

public interface UserDao {

    boolean isOrguserExistByUserid(Map<String, Object> params);

    boolean isOrguserExistByQywxuserid(Map<String, Object> params);

    String addOrguserAccQywx(Map<String, Object> params) throws Exception;

    List<B2B_USER> getOrguserByOrgid(Map<String, Object> params) throws Exception;

    List<B2B_USER> getOrguserByUserid(Map<String, Object> params) throws Exception;

    List<B2B_USER> getOrguserByQywxuserid(Map<String, Object> params) throws Exception;

    List<B2B_USER> getOrguserByUcmluseroid(Map<String, Object> params) throws Exception;

    List<B2B_USER> getOrguserByFsid(Map<String, Object> params) throws Exception;

    Map<String, Object> getUserZrzx(Map<String, Object> params) throws Exception;

    List<Map<String, Object>> getLeaderAuditInfo(Map<String, Object> params);

    Map<String, Object> getUcmluserinfoByFsid(Map<String, Object> params) throws Exception;

    List<Map<String, Object>> getUcmlAllUserinfo() throws Exception;

    Map<String, Object> getUcmluserinfoByUcmluseroid(Map<String, Object> params) throws Exception;
}
