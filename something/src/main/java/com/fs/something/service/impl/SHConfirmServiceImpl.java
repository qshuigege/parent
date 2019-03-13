package com.fs.something.service.impl;

import com.fs.something.pojo.B2B_USER;
import com.fs.something.repository.UserDao;
import com.fs.something.service.SHConfirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SHConfirmServiceImpl implements SHConfirmService {

    @Autowired
    private UserDao dao;

    @Override
    public boolean isOrguserExistByQywxuserid(Map<String, Object> params) {
        return dao.isOrguserExistByQywxuserid(params);
    }

    @Override
    public List<B2B_USER> getOrguserByQywxuserid(Map<String, Object> params) throws Exception {
        return dao.getOrguserByQywxuserid(params);
    }

    @Override
    public String addOrguserAccQywx(Map<String, Object> params) throws Exception {
        return dao.addOrguserAccQywx(params);
    }

    @Override
    public Map<String, Object> getUserZrzx(Map<String, Object> params) throws Exception {
        return dao.getUserZrzx(params);
    }
}
