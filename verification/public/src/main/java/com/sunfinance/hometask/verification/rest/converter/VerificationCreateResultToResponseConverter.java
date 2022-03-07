package com.sunfinance.hometask.verification.rest.converter;

import com.sunfinance.hometask.api.event.verification.VerificationCreateResult;
import com.sunfinance.hometask.api.rest.verification.VerificationCreateResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class VerificationCreateResultToResponseConverter {
    public VerificationCreateResponse convert(VerificationCreateResult verificationCreateResult) {
        return VerificationCreateResponse.builder()
                                         .id(verificationCreateResult.getId())
                                         .build();
    }
}
