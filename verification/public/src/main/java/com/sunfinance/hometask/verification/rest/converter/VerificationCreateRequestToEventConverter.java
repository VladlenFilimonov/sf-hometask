package com.sunfinance.hometask.verification.rest.converter;

import com.sunfinance.hometask.api.UserInfo;
import com.sunfinance.hometask.api.event.verification.VerificationCreateEvent;
import com.sunfinance.hometask.api.rest.verification.VerificationCreateRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class VerificationCreateRequestToEventConverter {

    public VerificationCreateEvent convert(VerificationCreateRequest request, UserInfo userInfo) {
        return VerificationCreateEvent.builder()
                                      .subject(request.getSubject())
                                      .userInfo(userInfo)
                                      .build();
    }
}
