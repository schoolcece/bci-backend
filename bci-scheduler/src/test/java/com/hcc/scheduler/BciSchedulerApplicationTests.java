package com.hcc.scheduler;

import com.hcc.scheduler.entity.ComputeNodeEntity;
import com.hcc.scheduler.mapper.ComputeNodeMapper;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BciSchedulerApplicationTests {

    @Autowired
    ComputeNodeMapper computeNodeMapper;

    @Autowired
    RedissonClient redissonClient;

    @Test
    void contextLoads() {
    }

    @Test
    void test1(){
        System.out.println(redissonClient);
    }

}
