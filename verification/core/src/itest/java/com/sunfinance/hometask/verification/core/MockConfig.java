package com.sunfinance.hometask.verification.core;

import com.sunfinance.hometask.verification.core.service.generator.CodeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.IdGenerator;

import java.math.BigInteger;
import java.time.Clock;

import static org.mockito.Mockito.mock;

@Configuration
public class MockConfig {

    @Bean
    @Primary
    public IdGenerator mockGenerator() {
        return mock(IdGenerator.class);
    }

    @Bean
    @Primary
    public CodeGenerator<BigInteger> codeGenerator() {
        return mock(CodeGenerator.class);
    }

    @Bean
    @Primary
    public Clock clockMock() {
        return mock(Clock.class);
    }
}
