package com.sunfinance.hometask.verification.core.service.converter;

import com.sunfinance.hometask.api.event.verification.VerificationConfirmationResult;
import com.sunfinance.hometask.verification.core.aggregate.Verification;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class VerificationToConfirmationResultConverter implements Converter<Verification, VerificationConfirmationResult> {

    @Override
    public VerificationConfirmationResult convert(Verification source) {
        return VerificationConfirmationResult.builder()
                                             .id(source.getId())
                                             .build();
    }
}
