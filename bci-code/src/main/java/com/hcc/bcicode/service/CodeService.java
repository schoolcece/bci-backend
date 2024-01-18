package com.hcc.bcicode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hcc.common.model.dto.CodeDTO;
import com.hcc.common.model.entity.CodeDO;
import com.hcc.common.model.vo.CodeVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    CodeDTO listCode(int paradigmId, int current);

    String getCodeUrlById(int codeId);
}
