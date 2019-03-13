/*
package com.example.demo.exception;

import com.example.demo.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("${server.error.path:/error}")
public class FSGlobalErrorController_bak implements ErrorController {
    @Autowired
    private FSGlobalHandlerExceptionResolver errorInfoBuilder;//错误信息的构建工具.

    private final static String DEFAULT_ERROR_VIEW = "error";//错误信息页

    */
/**
     * 情况1：若预期返回类型为text/html,则返回错误信息页(View).
     *//*

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request) {
        return new ModelAndView(DEFAULT_ERROR_VIEW, "errorInfo", errorInfoBuilder.getErrorInfo(request));
    }

    */
/**
     * 情况2：其它预期类型 则返回详细的错误信息(JSON).
     *//*

    @RequestMapping
    @ResponseBody
    public ErrorInfo error(HttpServletRequest request) {
        //return errorInfoBuilder.getErrorInfo(request);
        return errorInfoBuilder.getErrorInfo(request);
    }

    @Override
    public String getErrorPath() {//获取映射路径
        return errorInfoBuilder.getErrorProperties().getPath();
    }
}
*/
