package com.hcc.bcicode.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hcc.bcicode.mapper.CodeMapper;
import com.hcc.bcicode.service.CodeService;
import com.hcc.common.config.BCIConfig;
import com.hcc.common.constant.CustomConstants;
import com.hcc.common.enums.ErrorCodeEnum;
import com.hcc.common.exception.RTException;
import com.hcc.common.model.bo.UserInfoBO;
import com.hcc.common.model.dto.CodeDTO;
import com.hcc.common.model.entity.CodeDO;
import com.hcc.common.model.vo.CodeVO;
import com.hcc.common.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: service层 代码管理实现
 *
 * @Author: hcc
 * @Date: 2023/12/11
 */
@Service
@Slf4j
public class CodeServiceImpl extends ServiceImpl<CodeMapper, CodeDO> implements CodeService {

    @Autowired
    private CodeMapper codeMapper;

    @Autowired
    private BCIConfig.CodeConfig codeConfig;

    private final Logger logger = LoggerFactory.getLogger("代码服务日志记录");

    @Override
    public void uploadCode(int paradigmId, MultipartFile file) {
        UserInfoBO user = UserUtils.getUser();
        // 1. 鉴权（用户是否参加该范式的权限）
        checkPermissions(user, paradigmId);

        // 2. 文件非空判断及文件格式检验
        if (file.isEmpty() || !Objects.requireNonNull(file.getOriginalFilename()).endsWith(".tar.gz")) {
            throw new RTException(ErrorCodeEnum.PARAM_EXCEPTION.getCode(), ErrorCodeEnum.PARAM_EXCEPTION.getMsg());
        }

        // 3.文件MD5检验（如存在相同文件则无需再保存文件，todo： 相同文件属于不同用户发出预警）
        try {
            String md5DigestAsHex = DigestUtils.md5DigestAsHex(file.getBytes());
            String filename = file.getOriginalFilename();

            // 当前用户保存路径
            String savePath = codeConfig.getUrl() + "/" + paradigmId + "/" + user.getUserId() + "/" + md5DigestAsHex;
            File userDir = new File(savePath);
            if (!userDir.exists()) userDir.mkdirs();

            File userFile = new File(userDir, filename);

            List<CodeDO> codeExists = codeMapper.selectList(new QueryWrapper<CodeDO>().eq("md5", md5DigestAsHex));

            if (codeExists.isEmpty()) {
                // 没有相同文件，直接保存
                file.transferTo(userFile);
            } else {
                // 存在相同文件，准备创建软链接
                String existingPath = codeExists.get(0).getUrl() + "/" + codeExists.get(0).getFileName();
                File existingFile = new File(existingPath);

                if (!existingFile.exists()) {
                    throw new RTException(ErrorCodeEnum.SYSTEM_ERROR.getCode(), "已有记录的文件不存在，无法创建链接");
                }

                // 如果 userFile 是目录或已存在，删除它（防止软链接失败）
                if (userFile.exists()) {
                    if (userFile.isDirectory()) {
                        FileSystemUtils.deleteRecursively(userFile);
                    } else {
                        Files.delete(userFile.toPath());
                    }
                }

                // 创建软链接
                Files.createSymbolicLink(userFile.toPath(), existingFile.toPath());

                logger.warn("相同代码上传警告：用户 [{}]，软链接指向已有文件 [{}]", user.getUserId(), existingPath);
            }

            // 保存元数据到数据库
            CodeDO codeDO = CodeDO.builder()
                    .paradigmId(paradigmId)
                    .url(userFile.getParent())
                    .userId(user.getUserId())
                    .fileName(filename)
                    .md5(md5DigestAsHex)
                    .build();
            codeMapper.insert(codeDO);
        } catch (IOException e) {
            throw new RTException(ErrorCodeEnum.SYSTEM_ERROR.getCode(), ErrorCodeEnum.SYSTEM_ERROR.getMsg());
        }
    }

    @Override
    public CodeDTO listCode(int paradigmId, int current) {
        UserInfoBO user = UserUtils.getUser();
        return CodeDTO.builder()
                .codes(codeMapper
                        .selectPageByUserId(paradigmId, user.getUserId(), (current-1)*CustomConstants.PageSize.CODE_SIZE, CustomConstants.PageSize.CODE_SIZE))
                .total(codeMapper.selectCount(new QueryWrapper<CodeDO>().eq("paradigm_id", paradigmId).eq("user_id", user.getUserId()).eq("show_status", 1)))
                .build();

    }

    @Override
    public String getCodeUrlById(int codeId) {
        return codeMapper.getCodeUrlById(codeId) + "/" + codeMapper.getFileNameById(codeId);
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
