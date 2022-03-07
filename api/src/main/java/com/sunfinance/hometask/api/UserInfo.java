package com.sunfinance.hometask.api;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;

@Value
@Builder
@Jacksonized
public class UserInfo {

    @NotBlank
    String ipAddress;

    @NotBlank
    String userAgent;

}
