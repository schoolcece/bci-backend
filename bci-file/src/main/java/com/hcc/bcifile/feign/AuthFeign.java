package com.hcc.bcifile.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "bci-auth")
public interface AuthFeign {
    @GetMapping("/team/getTeamNameByUserIdAndEvent")
    String getTeamNameByUserIdAndEvent(@RequestParam("userId")int userId, @RequestParam("event") int event);
}
