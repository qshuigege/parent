package com.fs.something.controller.vo;


import com.fs.something.repository.pojo.SupplierInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FS on 2018/5/10.
 */
public class SuppliersVO extends BaseResult {
    private List<SupplierInfo> supplierLst = new ArrayList<SupplierInfo>();

    public List<SupplierInfo> getSupplierLst() {
        return supplierLst;
    }

    public void setSupplierLst(List<SupplierInfo> supplierLst) {
        this.supplierLst = supplierLst;
    }
}
