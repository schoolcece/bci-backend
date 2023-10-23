package com.hcc.code.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("bci-task")
public interface TaskFeign {

    @PostMapping("/task/deleteByCodeId")
    void deleteByCodeId(@RequestParam("codeId") int codeId);
}
