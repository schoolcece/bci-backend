package com.hcc.code.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hcc.code.entity.CodeEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 代码表
 *
 * @author hcc
 * @email 1301646502@qq.com
 * @date 2022-09-30 15:10:44
 */
public interface CodeService extends IService<CodeEntity> {

    void deleteById(int codeId);

    void upload(int type, MultipartFile file);

    void deleteByUserId(int userId);
//    int saveReturnId(CodeEntity code);

//    PageUtils queryPage(Map<String, Object> params);

}

