package com.hcc.bcicomponent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.hcc"})
public class BciComponentApplication {

	public static void main(String[] args) {
		SpringApplication.run(BciComponentApplication.class, args);
	}

}
