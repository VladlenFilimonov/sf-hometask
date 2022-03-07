package com.sunfinance.hometask.notification.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.sunfinance.hometask")
public class FeignConfig {
}
