package com.sunfinance.hometask.api.event.verification;

import com.sunfinance.hometask.api.VerificationSubject;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.UUID;

@Value
@Builder
@Jacksonized
public class VerificationCreatedEvent {

    @NotNull
    UUID id;
    @NotNull
    BigInteger code;
    @NotNull
    VerificationSubject subject;

}
