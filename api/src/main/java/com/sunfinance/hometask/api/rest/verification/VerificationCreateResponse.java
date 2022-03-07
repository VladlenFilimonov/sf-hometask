package com.sunfinance.hometask.api.rest.verification;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class VerificationCreateResponse {

    UUID id;

}
