package com.hcc.bcicode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.hcc"})
@EnableDiscoveryClient
public class BciCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BciCodeApplication.class, args);
    }

}
