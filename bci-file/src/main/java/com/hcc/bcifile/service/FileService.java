package com.hcc.bcifile.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void uploadFile(int paradigmId, MultipartFile file);
}
