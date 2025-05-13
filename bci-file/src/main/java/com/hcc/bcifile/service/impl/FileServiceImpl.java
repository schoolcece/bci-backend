package com.hcc.bcifile.service.impl;

import com.hcc.bcifile.mapper.FileMapper;
import com.hcc.bcifile.service.FileService;
import com.hcc.common.config.BCIConfig;
import com.hcc.common.constant.CustomConstants;
import com.hcc.common.enums.ErrorCodeEnum;
import com.hcc.common.exception.RTException;
import com.hcc.common.model.bo.UserInfoBO;
import com.hcc.common.model.entity.FileDO;
import com.hcc.common.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

    private final Logger logger = LoggerFactory.getLogger("文件服务日志记录");

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private BCIConfig.FileConfig fileConfig;

    @Override
    public void uploadFile(int paradigmId, MultipartFile file) {
        UserInfoBO user = UserUtils.getUser();

        // 1.鉴权
        checkPermissions(user, paradigmId);

        // 2.检查文件是否为空
        if (file.isEmpty()) {
            throw new RTException(ErrorCodeEnum.PARAM_EXCEPTION.getCode(), ErrorCodeEnum.PARAM_EXCEPTION.getMsg());
        }

        try {
            String filename = file.getOriginalFilename();
            long fileSize = file.getSize();
            String suffix = filename.substring(filename.lastIndexOf('.') + 1);

            String savePath = fileConfig.getUrl() + "/" + paradigmId + "/" + user.getUserId();
            File saveDir = new File(savePath);

            if (!saveDir.exists()) {
                boolean mkdirs = saveDir.mkdirs();
                if (!mkdirs) {
                    throw new RTException(ErrorCodeEnum.SYSTEM_ERROR.getCode(), "创建文件目录失败");
                }
            }

            String filePath = savePath + "/" + filename;
            File destinationFile = new File(filePath);

            file.transferTo(destinationFile);

            FileDO fileDO = FileDO.builder()
                    .paradigmId(paradigmId)
                    .userId(user.getUserId())
                    .url(savePath)
                    .fileName(filename)
                    .fileSize(fileSize)
                    .fileType(suffix)
                    .build();

            fileMapper.insert(fileDO);

            logger.info("文件已上传成功：用户ID={}，范式ID={}，文件保存路径={}", user.getUserId(), paradigmId, filePath);
        } catch (IOException e) {
            logger.error("文件上传失败", e);
            throw new RTException(ErrorCodeEnum.SYSTEM_ERROR.getCode(), ErrorCodeEnum.SYSTEM_ERROR.getMsg());
        }
    }

    private void checkPermissions(UserInfoBO user, int paradigmId) {
        if (user.isAdmin()) {
            return;
        }
        if (Optional.ofNullable(user.getPermissions()).orElseThrow(() ->
                        new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg()))
                .getOrDefault(paradigmId, 0) != CustomConstants.ApplicationStatus.APPROVED) {
            throw new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg());
        }
    }
}
