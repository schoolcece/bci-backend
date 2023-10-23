package com.hcc.task.feign;


import com.hcc.common.utils.R;
import com.hcc.task.entity.LogEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("bci-auth-server")
public interface UserFeign {

    @GetMapping("/auth/getTeamName")
    String getTeamName(@RequestParam("teamId") int teamId);
}
