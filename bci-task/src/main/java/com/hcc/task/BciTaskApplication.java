package com.hcc.task;

import feign.RequestInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableRedisHttpSession
@EnableFeignClients(basePackages = "com.hcc.task.feign")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.hcc"})
public class BciTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(BciTaskApplication.class, args);
    }
}
