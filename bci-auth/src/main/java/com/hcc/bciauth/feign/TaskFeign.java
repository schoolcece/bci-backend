package com.hcc.bciauth.feign;

import com.hcc.common.model.R;
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
@FeignClient(name = "bci-task", url = "http://127.0.0.1:8083")
public interface TaskFeign {

    @PostMapping("/task/deleteTaskByUserId")
    R deleteTaskByUserId(@RequestParam("userId") int userId);
}
