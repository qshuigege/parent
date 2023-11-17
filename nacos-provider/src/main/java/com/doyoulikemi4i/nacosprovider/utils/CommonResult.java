package com.doyoulikemi4i.nacosprovider.utils;

public class CommonResult<T> {
    public int code;
    public String msg;

    public CommonResult(){}

    public CommonResult(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
