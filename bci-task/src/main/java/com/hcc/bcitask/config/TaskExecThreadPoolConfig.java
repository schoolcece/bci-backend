package com.hcc.bcitask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Description: 执行算法任务的线程池配置类
 *
 * @Author: hcc
 * @Date: 2024/1/20
 */
@Configuration
@EnableAsync
public class TaskExecThreadPoolConfig {
    private static final int CORE_POOL_SIZE = 10;

    private static final int MAX_POOL_SIZE = 20;

    private static final int QUEUE_CAPACITY = 200;

    public static final String ALG_TASK_EXECUTOR = "alg_task_executor";

    /**
     * 算法任务执行线程池执行器配置
     * @return 算法任务执行线程池执行器bean
     *
     */
    @Bean(ALG_TASK_EXECUTOR)
    public Executor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        // 设置队列容量
        executor.setQueueCapacity(QUEUE_CAPACITY);
        // 设置线程活跃时间(秒)
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("ALG-Pool#Task");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }
}
