package com.hcc.bcicomponent.service.impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.netty.NettyDockerCmdExecFactory;
import com.hcc.bcicomponent.service.ComponentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;


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
            DefaultDockerClientConfig config = DefaultDockerClientConfig
                    .createDefaultConfigBuilder()
                    .withDockerHost("tcp://10.112.236.95:2375")
                    .build();
            dockerClient = DockerClientBuilder
                    .getInstance(config)
                    .withDockerCmdExecFactory(new NettyDockerCmdExecFactory())
                    .build();


            HashSet<String> set = new HashSet<>();
            set.add("test:1.0");
            BuildImageCmd buildImageCmd = dockerClient.buildImageCmd()
                    .withTarInputStream(file.getInputStream())
                    .withDockerfilePath("Dockerfile")
                    .withTags(set);


            buildImageCmd.exec(new BuildImageResultCallback() {
                @Override
                public void onNext(BuildResponseItem item) {
                    super.onNext(item);
                    System.out.println(3);
                    System.out.println(item.toString());
                    // 处理构建过程中的输出信息
                }
            }).awaitImageId(30, TimeUnit.MINUTES);


        }catch (Exception e){
            //与计算节点建立连接失败，状态回滚：代码状态、今日提交次数，抛出异常，交给controller层处理。
            //throw new LoadingException("计算节点"+ip+"不可用,请联系管理员！"+e.getLocalizedMessage(), codeEntity, countKey, node.getNodeId());
            System.out.println(e.getLocalizedMessage());
        }


    }
}
