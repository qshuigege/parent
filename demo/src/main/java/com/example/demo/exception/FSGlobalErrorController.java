package com.example.demo.exception;

import com.example.demo.utils.JsonResult;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("${server.error.path:/error}")
public class FSGlobalErrorController extends AbstractErrorController {
    private final ErrorProperties errorProperties;

    public FSGlobalErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        super(errorAttributes);
        this.errorProperties = serverProperties.getError();
    }

    @Override
    public String getErrorPath() {
        return this.errorProperties.getPath();
    }

    @RequestMapping(
            produces = {"text/html"}
    )
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView=new ModelAndView("error");
        Map<String, Object> errorMap=getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML));
        if(errorMap!=null) {
            /*timestamp status error message path*/
            modelAndView.addObject("timestamp",errorMap.get("timestamp"));
            modelAndView.addObject("error",errorMap.get("error"));
            modelAndView.addObject("status",errorMap.get("status"));
            modelAndView.addObject("message",errorMap.get("message"));
            modelAndView.addObject("path",errorMap.get("path"));
        }
        return modelAndView;
    }

    @RequestMapping
    public JsonResult error(HttpServletRequest request) {
        Map<String, Object> errorMap=getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        return JsonResult.fail(errorMap.get("error").toString(), errorMap);
    }

    protected boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces) {
        ErrorProperties.IncludeStacktrace include = this.getErrorProperties().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        } else {
            return include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM ? this.getTraceParameter(request) : false;
        }
    }

    protected ErrorProperties getErrorProperties() {
        return this.errorProperties;
    }
}
