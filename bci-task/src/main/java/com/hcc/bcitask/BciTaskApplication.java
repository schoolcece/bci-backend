package com.hcc.bcitask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.hcc"})
public class BciTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(BciTaskApplication.class, args);
    }

}
