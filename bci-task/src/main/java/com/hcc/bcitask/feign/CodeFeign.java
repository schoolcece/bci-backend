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
@FeignClient(name = "bci-code")
public interface CodeFeign {
    @GetMapping("/code/getCodeUrlById")
    String getCodeUrlById(@RequestParam("codeId")int codeId);

    @GetMapping("/code/getMd5ById")
    String getMd5ById(@RequestParam("codeId")int codeId);
}
