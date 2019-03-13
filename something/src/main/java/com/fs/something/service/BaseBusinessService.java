package com.fs.something.service;

import com.fs.something.repository.BaseOperationDAO;
import com.fs.something.repository.pojo.BusinessType;
import com.fs.something.repository.pojo.SupplierInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by FS on 2018/5/10.
 */
@Service
public class BaseBusinessService {

    @Autowired
    private BaseOperationDAO baseOperationDAO;

    public List<SupplierInfo> getCustomSuppliers(String customId, BusinessType businessType) {
        return baseOperationDAO.findCustomSuppliers(customId, businessType);
    }

    public String  testProc(String param){
        return baseOperationDAO.execProcTest(param);
    }
}
