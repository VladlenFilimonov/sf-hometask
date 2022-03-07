package com.sunfinance.hometask.api.rest.verification;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Value
@Builder
@Jacksonized
public class VerificationConfirmationRequest {

    @NotNull
    BigInteger code;

}
