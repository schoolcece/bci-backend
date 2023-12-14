package com.hcc.bcicompetition.controller;

import com.hcc.bcicompetition.service.EventService;
import com.hcc.common.annotation.Loggable;
import com.hcc.common.model.R;
import com.hcc.common.model.param.EventParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;

/**
 * Description: controller层 赛事管理
 *
 * @Author: hcc
 * @Date: 2023/12/11
 */
@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    /**
     * 发布赛事接口
     * @param eventParam
     * @return
     */
    @PostMapping("/publishEvent")
    @Loggable("发布赛事")
    public R publishEvent(@RequestBody EventParam eventParam) {
        return R.ok();
    }

    /**
     * 查看当前时刻是否处于赛事期间(内部远程调用接口)
     * @param curTime
     * @param event
     * @return
     */
    @GetMapping("/inTime")
    public boolean inTime(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")@RequestParam("curTime") Date curTime,
                          @RequestParam("event") int event) {
        return eventService.inTime(curTime, event);
    }

}
