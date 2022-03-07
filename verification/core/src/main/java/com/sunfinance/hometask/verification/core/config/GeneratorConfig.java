package com.sunfinance.hometask.verification.core.config;

import com.sunfinance.hometask.verification.core.config.properties.VerificationProperties;
import com.sunfinance.hometask.verification.core.service.generator.CodeGenerator;
import com.sunfinance.hometask.verification.core.service.generator.impl.NumericCodeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.IdGenerator;

import java.math.BigInteger;
import java.util.UUID;

@Configuration
public class GeneratorConfig {

    @Bean
    public IdGenerator idGenerator() {
        return UUID::randomUUID;
    }

    @Bean
    public CodeGenerator<BigInteger> numericCodeGenerator(VerificationProperties properties) {
        return new NumericCodeGenerator(properties.getCodeLength());
    }
}
