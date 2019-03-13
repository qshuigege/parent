package com.fs.something.service;

import com.fs.something.pojo.B2B_USER;

import java.util.List;
import java.util.Map;

public interface SHConfirmService {

    boolean isOrguserExistByQywxuserid(Map<String, Object> useridMap);

    List<B2B_USER> getOrguserByQywxuserid(Map<String, Object> params) throws Exception;

    String addOrguserAccQywx(Map<String, Object> params) throws Exception;

    Map<String, Object> getUserZrzx(Map<String, Object> params) throws Exception;
}
