package com.sunfinance.hometask.verification.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sunfinance.hometask")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
