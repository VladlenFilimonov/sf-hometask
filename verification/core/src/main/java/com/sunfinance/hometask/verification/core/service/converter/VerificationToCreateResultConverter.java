package com.sunfinance.hometask.verification.core.service.converter;

import com.sunfinance.hometask.api.event.verification.VerificationCreateResult;
import com.sunfinance.hometask.verification.core.aggregate.Verification;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class VerificationToCreateResultConverter implements Converter<Verification, VerificationCreateResult> {

    @Override
    public VerificationCreateResult convert(Verification source) {
        return VerificationCreateResult.builder()
                                       .id(source.getId())
                                       .build();
    }
}
