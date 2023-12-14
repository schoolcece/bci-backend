package com.hcc.bcicode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hcc.common.model.entity.CodeDO;
import org.springframework.web.multipart.MultipartFile;

/**
 * Description: service层 代码管理接口
 *
 * @Author: hcc
 * @Date: 2023/12/11
 */
public interface CodeService extends IService<CodeDO> {

    /**
     * 代码上传接口
     * @param paradigm
     * @param file
     */
    void uploadCode(int paradigm, MultipartFile file);
}
