package com.hcc.bcifile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.hcc"})
@EnableDiscoveryClient
public class BciFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(BciFileApplication.class, args);
    }

}
