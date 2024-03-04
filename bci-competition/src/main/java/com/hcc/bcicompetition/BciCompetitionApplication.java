package com.hcc.bcicompetition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.hcc"})
@EnableDiscoveryClient
public class BciCompetitionApplication {

    public static void main(String[] args) {
        SpringApplication.run(BciCompetitionApplication.class, args);
    }

}
