package com.sunfinance.hometask.verification.core.service.generator.impl;

import com.sunfinance.hometask.verification.core.service.generator.CodeGenerator;

import java.math.BigInteger;
import java.util.Random;

public class NumericCodeGenerator implements CodeGenerator<BigInteger> {

    private static final Random RND = new Random();
    private final Integer codeLength;

    public NumericCodeGenerator(Integer codeLength) {
        if (codeLength < 1) {
            throw new RuntimeException("Code length can't be less than 1");
        }
        this.codeLength = codeLength;
    }

    @Override
    public BigInteger generateCode() {
        StringBuilder sb = new StringBuilder(codeLength);
        var rand = RND.nextInt(10);
        var firstDigit = rand > 0 ? rand : 1;
        sb.append(firstDigit);
        for (int i = 0; i < codeLength - 1; i++)
            sb.append(RND.nextInt(10));
        return new BigInteger(sb.toString());
    }
}
