package com.hcc.bciauthserver.feign;

import com.hcc.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient("bci-code")
public interface CodeFeign {

    @PostMapping("/code/deleteByUserId")
    public R deleteByUserId(@RequestParam("userId") int userId);
}
