package com.hcc.bcicomponent.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Description: 组件管理 service层
 *
 * @Author: hcc
 * @Date: 2023/12/25
 */
public interface ComponentService {

    /**
     * 镜像打包推送
     * @param url
     * @param file
     */
    void pushImage(String url, MultipartFile file);
}
