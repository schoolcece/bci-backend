package com.hcc.bciauthserver.feign;


import com.hcc.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("bci-task")
public interface TaskFeign {

    @PostMapping("/task/deleteByUserId")
    public R deleteByUserId(@RequestParam("userId") int userId);
}
