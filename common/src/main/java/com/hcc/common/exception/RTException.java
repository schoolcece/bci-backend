package com.hcc.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 自定义运行时异常
 */
@Data
@AllArgsConstructor
public class RTException extends RuntimeException {

    /**
     * 异常码
     */
    private int code;

    /**
     * 异常信息
     */
    private String msg;
}
