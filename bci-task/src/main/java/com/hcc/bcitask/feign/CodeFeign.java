package com.hcc.bcitask.feign;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * Description: 远程调用代码服务接口
 *
 * @Author: hcc
 * @Date: 2024/1/9
 */
@FeignClient
public interface CodeFeign {
    String getCodeUrlById(int codeId);
}
