package com.hcc.log.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hcc.common.exception.BizCodeEnum;
import com.hcc.common.exception.RRException;
import com.hcc.common.utils.R;
import com.hcc.log.entity.LogEntity;
import com.hcc.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log")
public class LogController {

    LogService logService;

    @Autowired
    public LogController(LogService logService){
        this.logService = logService;
    }

    /**
     * 获取任务日志 get请求
     * @param taskId
     * @return
     */
    @GetMapping("/getLogByTaskId")
    public R getLogByTaskId(@RequestParam int taskId){
        QueryWrapper<LogEntity> logEntityQueryWrapper = new QueryWrapper<>();
        logEntityQueryWrapper.eq("task_id",taskId);
        LogEntity logEntity = logService.getOne(new QueryWrapper<LogEntity>().eq("task_id", taskId));
        if(logEntity == null) {
            throw new RRException(BizCodeEnum.LOG_NOT_EXIST.getCode(), BizCodeEnum.LOG_NOT_EXIST.getMsg());
        }
        return R.ok().put("log",logEntity);
    }

    /**
     * 存日志信息 post请求
     * @param log
     * @return
     */
    @PostMapping("/saveLog")
    public R saveLog(@RequestBody LogEntity log){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String info = (String) authentication.getPrincipal();
        if (!info.equals("innerVisit")){
            throw new RRException(BizCodeEnum.INNER_REJECT.getCode(), BizCodeEnum.INNER_REJECT.getMsg());
        }
        logService.save(log);
        return R.ok();
    }
}
