package com.hcc.bcitask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.hcc"})
public class BciTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(BciTaskApplication.class, args);
    }

}
