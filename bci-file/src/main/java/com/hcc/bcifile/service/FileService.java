package com.hcc.bcifile.service;

import com.hcc.common.model.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void uploadFile(int paradigmId, MultipartFile file);

    FileDTO listFileByParadigm(int paradigmId, int curPage);
}
