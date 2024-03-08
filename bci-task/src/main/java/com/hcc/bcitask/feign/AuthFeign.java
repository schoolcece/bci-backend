package com.hcc.bcitask.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Description: 远程调用代码服务接口
 *
 * @Author: hcc
 * @Date: 2024/1/9
 */
@FeignClient(name = "bci-auth")
public interface AuthFeign {
    @GetMapping("/team/getTeamNameById")
    String getTeamName(@RequestParam("teamId")int teamId);
}
