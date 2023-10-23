package com.hcc.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.hcc.task.entity.*;
import com.hcc.task.mapper.CollectionTaskMapper;
import com.hcc.task.service.CollectionTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Slf4j
@RefreshScope
@Service
public class CollectionTaskServiceImpl extends ServiceImpl<CollectionTaskMapper, CollectionTaskEntity> implements CollectionTaskService {

    @Value("${collection.image}")
    String collectionImage;

    @Value("${collection.srate}")
    int srate;

    @Value("${collection.nchan}")
    int nchan;

    @Value("${collection.timebuffer}")
    float timebuffer;

    @Override
    public void runCollectionTask(CollectionVo collectionVo) {
        String ip = "10.112.236.95";
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://"+ip+":2375")
                .build();
        DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();

        //创建本次执行的容器
        String cmd = "python main.py";
        HostConfig hostConfig = new HostConfig();
        hostConfig.withExtraHosts(new String[]{"server:"+collectionVo.getKafkaIP()});
        CreateContainerResponse container = dockerClient.createContainerCmd(collectionImage)
                .withEnv("deviceName="+collectionVo.getDevice(),"hostname="+collectionVo.getDatasourceIP(),"port="+collectionVo.getDatasourcePORT(),
                        "srate="+srate,"nchan="+nchan,"timebuffer="+timebuffer,"topic="+collectionVo.getKafkaTOPIC())
                .withHostConfig(hostConfig)
                .withCmd("/bin/sh" , "-c", cmd).exec();

        dockerClient.startContainerCmd(container.getId()).exec();
    }
}