package com.fs.myerp.service;

import com.fs.diyutils.JsonResponse;

import javax.servlet.http.HttpServletRequest;

public interface AopTestService {

    JsonResponse testAop(HttpServletRequest request);

}
