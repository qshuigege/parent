package com.fs.myerp.config.security;

import com.fs.diyutils.JsonResponse;
import com.fs.myerp.utils.JsonUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.session.SessionInformationExpiredEvent;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HttpServletResponseUtil {

    public static void loginSuccessResponse(HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(JsonUtils.objectToJson(JsonResponse.success(authentication)));
        out.flush();
        out.close();
    }

    public static void loginFailureResponse(HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(JsonUtils.objectToJson(JsonResponse.fail(exception.getMessage())));
        out.flush();
        out.close();
    }

    public static void onExpiredSessionDetectedResponse(SessionInformationExpiredEvent event) throws IOException {
        HttpServletResponse response = event.getResponse();
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(JsonUtils.objectToJson(JsonResponse.fail("你的账户已在另一地点登录！")));
        out.flush();
        out.close();
    }

}
