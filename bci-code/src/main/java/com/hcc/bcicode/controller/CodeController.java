package com.hcc.bcicode.controller;

import com.hcc.bcicode.service.CodeService;
import com.hcc.common.annotation.Loggable;
import com.hcc.common.model.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Description: controller层 代码管理
 *
 * @Author: hcc
 * @Date: 2023/12/11
 */
@RestController
@RequestMapping("/code")
public class CodeController {

    @Autowired
    private CodeService codeService;

    /**
     * 代码上传接口
     * @param paradigmId
     * @param file
     * @return
     */
    @PostMapping("/uploadCode")
    @Loggable("代码上传")
    public R uploadCode(@RequestParam("paradigmId") int paradigmId, @RequestParam("file")MultipartFile file) {
        codeService.uploadCode(paradigmId, file);
        return R.ok();
    }

    /**
     * 代码查询接口
     * @param paradigmId
     * @param current
     * @return
     */
    @GetMapping("/listCode")
    public R list(@RequestParam int paradigmId,
                  @RequestParam(value = "current", defaultValue = "1") Integer current){
        return R.ok().put("data", codeService.listCode(paradigmId, current));
    }

}
