package com.hcc.code.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hcc.code.entity.CodeEntity;
import com.hcc.code.feign.TaskFeign;
import com.hcc.code.mapper.CodeMapper;
import com.hcc.code.service.CodeService;
import com.hcc.common.exception.BizCodeEnum;
import com.hcc.common.exception.RRException;
import com.hcc.common.utils.UserUtils;
import com.hcc.common.vo.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;


@Service("codeService")
@RefreshScope
public class CodeServiceImpl extends ServiceImpl<CodeMapper, CodeEntity> implements CodeService {

    CodeMapper codeMapper;

    TaskFeign taskFeign;

    @Value("${code.url}")
    private String path;

    public CodeServiceImpl(CodeMapper codeMapper, TaskFeign taskFeign){
        this.codeMapper = codeMapper;
        this.taskFeign = taskFeign;
    }

    @Transactional
    @Override
    public void deleteById(int codeId) {
        codeMapper.deleteById(codeId);
        taskFeign.deleteByCodeId(codeId);
    }

    @Override
    public void upload(int type, MultipartFile file) {
        UserInfo userInfo = UserUtils.getUser();
        //1. 检查上传文件是否为空
        if (file.isEmpty()) {
            throw new RRException(BizCodeEnum.FILE_EMPTY.getCode(), BizCodeEnum.FILE_EMPTY.getMsg());
        }

        //2. 保存代码文件（直接存入服务器本地磁盘）
        String uuid = UUID.randomUUID().toString().replace("-","");
        String savePath = path + "/" + type +"/" + uuid;
        try{
            File filePath = new File(savePath, file.getOriginalFilename());
            filePath.mkdirs();
            file.transferTo(filePath);
        }catch (Exception e){
            throw new RRException(BizCodeEnum.FILE_SAVE_FAILED.getCode(), BizCodeEnum.FILE_SAVE_FAILED.getMsg());
        }

        //3. 插入数据库信息
        CodeEntity code = new CodeEntity();
        code.setType(type);
        code.setUrl(savePath+"/"+file.getOriginalFilename());
        code.setUserId(userInfo.getUserId());
        code.setFileName(file.getOriginalFilename());
        code.setStatus(0);
        code.setCreateTime(new Timestamp(new Date().getTime()));
        try {
            codeMapper.insert(code);
        }catch (Exception e){
            throw new RRException(BizCodeEnum.FILE_SAVE_FAILED.getCode(), BizCodeEnum.FILE_SAVE_FAILED.getMsg());
        }
    }

    @Override
    public void deleteByUserId(int userId) {
        codeMapper.deleteByUserId(userId);
    }
}