package com.fs.myerp.service.impl;

import com.fs.diyutils.JsonResponse;
import com.fs.myerp.service.AopTestService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AopTestServiceImpl implements AopTestService {
    @Override
    public JsonResponse testAop(HttpServletRequest request) {
        return JsonResponse.success(request.getParameter("loginId"));
    }
}
