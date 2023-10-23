package com.hcc.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.hcc")
public class BciLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BciLogApplication.class, args);
    }

}
