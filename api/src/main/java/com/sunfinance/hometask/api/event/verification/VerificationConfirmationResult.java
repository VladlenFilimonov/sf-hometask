package com.sunfinance.hometask.api.event.verification;

import com.sunfinance.hometask.api.error.ResultError;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class VerificationConfirmationResult {

    UUID id;
    ResultError error;

}
