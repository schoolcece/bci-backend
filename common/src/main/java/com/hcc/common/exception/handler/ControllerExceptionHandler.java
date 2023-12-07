package com.hcc.common.exception.handler;

import com.hcc.common.enums.ErrorCodeEnum;
import com.hcc.common.exception.RTException;
import com.hcc.common.model.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * controller层统一异常处理
 */
@RestControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger("统一异常处理");

    /**
     * 自定义运行时异常
     * @param rtException
     * @return
     */
    @ExceptionHandler(RTException.class)
    public R handlerRTException(RTException rtException) {
        return R.error(rtException.getCode(), rtException.getMsg());
    }

    /**
     * 参数校验异常
     * @param bindException
     * @return
     */
    @ExceptionHandler(BindException.class)
    public R handlerBindException(BindException bindException) {
        return R.error(ErrorCodeEnum.PARAM_EXCEPTION.getCode(), bindException.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 系统未知异常
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    public R handlerException(Exception exception) {
        logger.error("系统未知异常原因： {}", exception.toString());
        return R.error(ErrorCodeEnum.UN_KNOW_EXCEPTION.getCode(), ErrorCodeEnum.UN_KNOW_EXCEPTION.getMsg());
    }
}
