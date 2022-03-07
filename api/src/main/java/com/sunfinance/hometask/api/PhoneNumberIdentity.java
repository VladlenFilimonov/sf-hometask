package com.sunfinance.hometask.api;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Value
@Builder
@Jacksonized
public class PhoneNumberIdentity implements SubjectIdentity {

    @NotBlank(message = "Phone number identity must not be blank")
    @Pattern(regexp = "^(\\+)?([ 0-9]){10,16}$", message = "Malformed phone number identity")
    String val;

}
