package com.hcc.bcitask.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Description: 远程调用代码服务接口
 *
 * @Author: hcc
 * @Date: 2024/1/9
 */
@FeignClient(name = "bci-code", url = "http://127.0.0.1:8081")
public interface CodeFeign {
    @GetMapping("/code/getCodeUrlById")
    String getCodeUrlById(int codeId);
}
