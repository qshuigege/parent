package com.fs.everything.repository;

import java.util.List;
import java.util.Map;

public interface UCML_UserDao {

    Map<String, Object> getUcmluserinfoByEmail(Map<String, Object> params) throws Exception;

    Map<String, Object> getUcmluserinfoByFsid(Map<String, Object> params) throws Exception;

    List<Map<String, Object>> getUcmlAllUserinfo() throws Exception;

}
