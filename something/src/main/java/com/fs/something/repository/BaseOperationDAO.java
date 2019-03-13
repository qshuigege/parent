package com.fs.something.repository;


import com.fs.something.repository.pojo.BusinessType;
import com.fs.something.repository.pojo.SupplierInfo;

import java.util.List;

/**
 * Created by FS on 2018/5/10.
 */
public interface BaseOperationDAO {
    /**
     * 得到当前客户的供应商信息
     *
     * @param customId
     * @param businessType
     * @return
     */
    public List<SupplierInfo> findCustomSuppliers(String customId, BusinessType businessType);


    public String   execProcTest(String param);
}
