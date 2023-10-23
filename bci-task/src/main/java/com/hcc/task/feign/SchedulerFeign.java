package com.hcc.task.feign;


import com.hcc.common.vo.NodeVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("bci-scheduler")
public interface SchedulerFeign {

    @GetMapping("/ComputeNode/getOne")
    public NodeVo getOne();

    @PostMapping("/ComputeNode/updateNode")
    public void updateNode(@RequestParam("nodeId") int nodeId);
}
