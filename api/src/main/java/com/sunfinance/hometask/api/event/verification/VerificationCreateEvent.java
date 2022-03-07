package com.sunfinance.hometask.api.event.verification;

import com.sunfinance.hometask.api.UserInfo;
import com.sunfinance.hometask.api.VerificationSubject;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Value
@Builder
@Jacksonized
public class VerificationCreateEvent {

    @Valid
    @NotNull
    UserInfo userInfo;

    @Valid
    @NotNull
    VerificationSubject subject;

}
