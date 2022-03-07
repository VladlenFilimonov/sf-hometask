package com.sunfinance.hometask.verification.rest.error;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ErrorMessage {

    String code;
    String description;
    String message;

}
