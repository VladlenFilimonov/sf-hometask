package com.sunfinance.hometask.api.event.verification;

import com.sunfinance.hometask.api.UserInfo;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.UUID;

@Value
@Builder
@Jacksonized
public class VerificationConfirmationEvent {

    @NotNull
    UUID verificationId;
    @NotNull
    UserInfo userInfo;
    @NotNull
    BigInteger code;

}
