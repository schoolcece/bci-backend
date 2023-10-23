package com.hcc.task.feign;

import com.hcc.common.utils.R;
import com.hcc.task.entity.LogEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("bci-log")
public interface LogFeign {
    @PostMapping("/log/saveLog")
    R saveLog(@RequestBody LogEntity log);
}
