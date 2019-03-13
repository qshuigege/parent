package com.fs.something.controller;

import com.fs.something.controller.vo.SuppliersVO;
import com.fs.something.repository.pojo.BusinessType;
import com.fs.something.repository.pojo.SupplierInfo;
import com.fs.something.service.BaseBusinessService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by FS on 2018/5/10.
 */
@RestController
@RequestMapping("/api/import/purchase/")
public class BaseInfoController {
    private static Log log = LogFactory.getLog(BaseInfoController.class);
    @Autowired
    private BaseBusinessService baseBusinessService;


    /**
     * 得到当前客户的供应商信息
     *
     * @param customId
     * @param businessCode
     * @return
     */
    @RequestMapping(value = "baseinfo/supplier", method = RequestMethod.GET)
    public SuppliersVO queryCustomSupplierInfo(@RequestParam(value = "customId") String customId, @RequestParam(value = "businessCode") String businessCode) {
        SuppliersVO result = new SuppliersVO();
        if (null == customId || null == businessCode) {
            result.setCause("param is null,verify param fail");
            return result;
        }
        if (log.isInfoEnabled()) {
            log.info("begin get supplier info... customId->" + customId + ",bussinessCode->" + businessCode);
        }
        BusinessType businessType = BusinessType.getBusinessType(businessCode);
        List<SupplierInfo> supplierInfoList = baseBusinessService.getCustomSuppliers(customId, businessType);
        if (null == supplierInfoList) {
            result.setCause("no supplier");
            return result;
        }
        result.setResult("success");
        result.setCause("");
        result.setSupplierLst(supplierInfoList);

        if (log.isInfoEnabled()) {
            log.info("finish get supplier info. customId->" + customId + ",bussinessCode->" + businessCode);
        }
        return result;
    }

}
