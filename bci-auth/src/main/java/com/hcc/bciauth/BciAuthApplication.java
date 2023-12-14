package com.hcc.bciauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.hcc"})
public class BciAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(BciAuthApplication.class, args);
    }

}
