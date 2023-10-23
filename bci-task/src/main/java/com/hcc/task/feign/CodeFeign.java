package com.hcc.task.feign;

import com.hcc.common.utils.R;
import com.hcc.task.entity.CodeEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("bci-code")
public interface CodeFeign {
    @PostMapping("/code/updateStatusReturnCode")
    CodeEntity updateStatusReturnCode(@RequestParam("codeId") int codeId, @RequestParam("status") int status);


    @PostMapping("/code/updateCode")
    R updateCode(@RequestBody CodeEntity codeEntity);

    @GetMapping("/code/getCodeById")
    CodeEntity getCodeById(@RequestParam("codeId") int codeId);
}
