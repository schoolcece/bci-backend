package com.hcc.task.exception.handler;

import com.hcc.common.exception.BizCodeEnum;
import com.hcc.task.exception.RRException;
import com.hcc.common.utils.R;
import com.hcc.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @Autowired
    TaskService taskService;

    @ExceptionHandler(RRException.class)
    public R handlerRRException(RRException re){
        return R.error(re.getCode(),re.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public R handlerException(Exception e){
        return R.error(BizCodeEnum.UN_KNOW_EXCEPTION.getCode(), e.getMessage());
    }

//    @ExceptionHandler(IOException.class)
//    public R handlerIOException(IOException ioe){
//        return R.error(240,"文件上传失败，请联系管理员！"+ioe.getMessage().toString());
//    }

//    @ExceptionHandler(InitializationException.class)
//    public R handlerINException(InitializationException ie){
//        taskService.handlerCPException(ie.getCodeEntity(),ie.getCountKey());
//        return R.error(ie.getCode(), ie.getMsg());
//    }
//
//    @ExceptionHandler(LoadingException.class)
//    public R handlerLDException(LoadingException le){
//        taskService.handlerLeException(le.getCodeEntity(), le.getCountKey(), le.getNodeId());
//        return R.error(le.getCode(), le.getMsg());
//    }
//
//    @ExceptionHandler(RunningException.class)
//    public R handlerRuException(RunningException re){
//        taskService.handlerRuException(re.isCancel(), re.getCodeEntity(),re.getCountKey(), re.getNodeId(), re.getTaskId());
//        return R.error(re.getCode(), re.getMsg());
//    }

}
