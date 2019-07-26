package com.fs.myerp.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationUtils {

    public static Map<String, Object> getFailMsg(BindingResult bindingResult){
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        Map<String, Object> map = new HashMap<>();
        for (ObjectError error : allErrors) {
            if (error instanceof FieldError){
                map.put(((FieldError) error).getField(), error.getDefaultMessage());
            }else {
                map.put(error.getObjectName(), error);
            }
        }
        return map;
    }
}
