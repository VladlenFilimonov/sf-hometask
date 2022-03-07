package com.sunfinance.hometask.api.event.verification;

import com.sunfinance.hometask.api.VerificationSubject;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
@Builder
@Jacksonized
public class VerificationConfirmationFailedEvent {

    @NotNull
    UUID id;
    @NotNull
    VerificationSubject subject;

}
