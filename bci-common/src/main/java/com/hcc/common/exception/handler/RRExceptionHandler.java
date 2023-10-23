package com.hcc.common.exception.handler;

import com.hcc.common.exception.BizCodeEnum;
import com.hcc.common.exception.RRException;
import com.hcc.common.utils.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RRExceptionHandler {

    @ExceptionHandler(RRException.class)
    public R handlerRRException(RRException re){
        return R.error(re.getCode(),re.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public R handlerException(Exception e){
        return R.error(BizCodeEnum.UN_KNOW_EXCEPTION.getCode(), e.getMessage());
    }

}
