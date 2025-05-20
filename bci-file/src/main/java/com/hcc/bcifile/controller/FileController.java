package com.hcc.bcifile.controller;

import com.hcc.bcifile.service.FileService;
import com.hcc.common.annotation.Loggable;
import com.hcc.common.model.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/listFile")
    public R listFileByParadigm(@RequestParam int paradigmId, @RequestParam int curPage) {
        return R.ok().put("data", fileService.listFileByParadigm(paradigmId, curPage));
    }
}
