package com.hcc.bcicomponent.controller;

import com.hcc.bcicomponent.service.ComponentService;
import com.hcc.common.model.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Description: 组件管理 controller层
 *
 * @Author: hcc
 * @Date: 2023/12/25
 */
@RestController
@RequestMapping("/component")
public class ComponentController {

    @Autowired
    private ComponentService componentService;

    @PostMapping("/pushImage")
    public R pushImage(@RequestParam(name = "url", required = false) String url,
                       @RequestParam(name = "file", required = false) MultipartFile file) {
        componentService.pushImage(url, file);
        return R.ok();
    }
}
