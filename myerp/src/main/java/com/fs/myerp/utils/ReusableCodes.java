package com.fs.myerp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReusableCodes {

    private static Logger log = LoggerFactory.getLogger(ReusableCodes.class);

    public static int calcRowindex(String pageNumber, String pageSize) throws Exception{
        try {
            return (Integer.parseInt(pageNumber) - 1) * Integer.parseInt(pageSize);
        }catch (Exception e){
            log.error("pageNumber({})和pageSize({})转成整数异常！-->{}",pageNumber, pageSize ,e.getMessage());
            throw new Exception("pageNumber("+pageNumber+")和pageSize("+pageSize+")转成整数异常！-->"+e);
        }
    }
}
