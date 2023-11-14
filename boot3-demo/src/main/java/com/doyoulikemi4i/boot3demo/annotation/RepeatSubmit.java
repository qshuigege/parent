package com.doyoulikemi4i.boot3demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fangkun
 * @create 2023-05-10-13:35
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmit {
    /**
     * 时间间隔
     * @return
     */
    int interval() default 5000;

    /**
     * 错误信息
     * @return
     */
    String message() default "不允许重复提交，请稍后再试！";
}
