package com.sunfinance.hometask.api.rest.verification;

import com.sunfinance.hometask.api.VerificationSubject;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Value
@Builder
@Jacksonized
public class VerificationCreateRequest {

    @Valid
    @NotNull(message = "Subject must not be blank")
    VerificationSubject subject;

}
