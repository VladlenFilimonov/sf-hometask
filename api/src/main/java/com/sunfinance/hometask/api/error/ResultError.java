package com.sunfinance.hometask.api.error;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ResultError {

    String message;
    ErrorCode code;

}
