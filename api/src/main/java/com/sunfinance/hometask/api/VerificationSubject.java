package com.sunfinance.hometask.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sunfinance.hometask.api.rest.verification.SubjectDeserializer;
import com.sunfinance.hometask.api.rest.verification.SubjectSerializer;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Value
@Builder
@JsonDeserialize(using = SubjectDeserializer.class)
@JsonSerialize(using = SubjectSerializer.class)
public class VerificationSubject {

    @Valid
    @NotNull(message = "Identity must not be blank")
    SubjectIdentity identity;

    @NotNull(message = "Type must not be blank")
    SubjectType type;

}
