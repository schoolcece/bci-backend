package com.hcc.bcicomponent.service.impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.netty.NettyDockerCmdExecFactory;
import com.github.dockerjava.transport.DockerHttpClient;
import com.hcc.bcicomponent.service.ComponentService;
import com.hcc.common.enums.ErrorCodeEnum;
import com.hcc.common.exception.RTException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashSet;

/**
 * Description: 组件管理 servcie实现
 *
 * @Author: hcc
 * @Date: 2023/12/25
 */
@Service
public class ComponentServiceImpl implements ComponentService {

    @Override
    public void pushImage(String url, MultipartFile file) {
        DockerClient dockerClient = null;
        try{
            //尝试与计算节点建立连接
            System.out.println("000");

            DefaultDockerClientConfig config = DefaultDockerClientConfig
                    .createDefaultConfigBuilder()
                    .withDockerHost("tcp://10.112.236.95:2375")
                    .build();
            System.out.println("0111");
            dockerClient = DockerClientBuilder
                    .getInstance(config)
                    .withDockerCmdExecFactory(new NettyDockerCmdExecFactory())
                    .build();
            System.out.println("0222");
        }catch (Exception e){
            System.out.println(111);
            //与计算节点建立连接失败，状态回滚：代码状态、今日提交次数，抛出异常，交给controller层处理。
            //throw new LoadingException("计算节点"+ip+"不可用,请联系管理员！"+e.getLocalizedMessage(), codeEntity, countKey, node.getNodeId());
            System.out.println(e.getStackTrace().toString());
        }
        System.out.println(222);
        if (dockerClient == null) {
            return;
        }

    }
}
