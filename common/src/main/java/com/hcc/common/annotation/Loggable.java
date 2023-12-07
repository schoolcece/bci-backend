package com.hcc.common.annotation;

import java.lang.annotation.*;

/**
 * 开启记录日志注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
    /**
     * 接口说明
     * @return
     */
    String value();
}
