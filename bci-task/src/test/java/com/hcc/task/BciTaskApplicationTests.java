package com.hcc.task;

import com.hcc.common.utils.R;
import com.hcc.common.utils.TokenHolder;
import com.hcc.task.controller.TaskController;
import com.hcc.task.feign.SchedulerFeign;
import com.hcc.task.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.*;

@Slf4j
@SpringBootTest
class BciTaskApplicationTests {


    @Autowired
    SchedulerFeign schedulerFeign;

    @Autowired
    TaskService taskService;

    @Autowired
    private ThreadPoolExecutor executor;

    @Value("${image.names}")
    String[] images;

    @Value("${map.hostUrls}")
    String[] hostUrls;

    @Autowired
    RedisTemplate<String, Long> redisTemplateForCount;

    @Autowired
    TaskController taskController;

//    @Autowired
//    String[] images;
    @Test
    void test2() throws InterruptedException {
        R scoresRank = taskService.getScoresRank(0, 0, 1);
    }
    @Test
    void contextLoads() {
    }

    @Test
    void test3() throws InterruptedException {

        ThreadPoolExecutor doReq = new ThreadPoolExecutor(2, 100,
                10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        ThreadPoolExecutor doWork = new ThreadPoolExecutor(2, 100,
                10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        Random random = new Random();
        for (int i=0;i<50;i++){
            //第 i 个请求进来
            CompletableFuture.runAsync(()->{
                String req = UUID.randomUUID().toString();
                String token = UUID.randomUUID().toString();
                System.out.println("请求"+req+"的token是：" + token);
                TokenHolder.saveToken(token);
//                System.out.println("线程"+req+"设置token");
                CompletableFuture.runAsync(()->{

                    int t = random.nextInt(20);
                    try {
                        Thread.sleep(t*1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("处理请求" + req + "的线程是： " + Thread.currentThread().getName());
                    System.out.println("请求" + req + "的token是： " + TokenHolder.getToken());
//                    TokenHolder.removeToken();
                }, doWork);
            }, doReq);
        }
        Thread.sleep(1000000);
    }




}




