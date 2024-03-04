package com.hcc.bciauth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Description: 远程调用接口：比赛信息
 *
 * @Author: hcc
 * @Date: 2023/12/12
 */
@FeignClient(name = "bci-competition")
public interface CompetitionFeign {

    @GetMapping("/event/inTime")
    boolean inTime(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")@RequestParam("curTime") Date curTime,
                   @RequestParam("event") int event);
}
