package com.hcc.bcicompetition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.hcc"})
public class BciCompetitionApplication {

    public static void main(String[] args) {
        SpringApplication.run(BciCompetitionApplication.class, args);
    }

}
