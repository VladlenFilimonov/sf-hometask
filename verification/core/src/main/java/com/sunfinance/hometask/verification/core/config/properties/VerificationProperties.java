package com.sunfinance.hometask.verification.core.config.properties;

import lombok.Data;

import java.time.Duration;

@Data
public class VerificationProperties {

    private Duration ttl = Duration.ofMinutes(5L);
    private Integer codeLength = 8;
    private Integer maxVerificationAttempts = 5;

}
