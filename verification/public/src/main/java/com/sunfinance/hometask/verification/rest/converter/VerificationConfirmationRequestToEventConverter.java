package com.sunfinance.hometask.verification.rest.converter;

import com.sunfinance.hometask.api.UserInfo;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationEvent;
import com.sunfinance.hometask.api.rest.verification.VerificationConfirmationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class VerificationConfirmationRequestToEventConverter {
    public VerificationConfirmationEvent convert(VerificationConfirmationRequest createRequest, UserInfo userInfo, UUID id) {
        return VerificationConfirmationEvent.builder()
                                            .verificationId(id)
                                            .code(createRequest.getCode())
                                            .userInfo(UserInfo.builder()
                                                              .ipAddress(userInfo.getIpAddress())
                                                              .userAgent(userInfo.getUserAgent())
                                                              .build())
                                            .build();
    }
}
