package com.sunfinance.hometask.verification.core.service.generator.impl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumericCodeGeneratorTest {

    private static final Integer CODE_LENGTH = 7;

    private final NumericCodeGenerator generator = new NumericCodeGenerator(CODE_LENGTH);

    @Test
    public void shouldGenerateNumericCode() {

        var bigInteger = generator.generateCode();
        assertEquals(CODE_LENGTH, (Integer) bigInteger.toString().length());
    }

}