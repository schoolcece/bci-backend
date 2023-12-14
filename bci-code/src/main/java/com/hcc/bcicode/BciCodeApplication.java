package com.hcc.bcicode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.hcc"})
public class BciCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BciCodeApplication.class, args);
    }

}
