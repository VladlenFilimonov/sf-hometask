package com.sunfinance.hometask.api;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
@Builder
@Jacksonized
public class EmailIdentity implements SubjectIdentity {

    @Email(message = "Malformed email identity")
    @NotBlank(message = "Email identity must not be blank")
    String val;
}
