package com.hcc.bcifile.controller;

import com.hcc.bcifile.service.FileService;
import com.hcc.common.annotation.Loggable;
import com.hcc.common.model.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/uploadFile")
    @Loggable("文件上传")
    public R uploadFile(@RequestParam("paradigmId") int paradigmId, @RequestParam("file")MultipartFile file) {
        fileService.uploadFile(paradigmId, file);
        return R.ok();
    }
}
