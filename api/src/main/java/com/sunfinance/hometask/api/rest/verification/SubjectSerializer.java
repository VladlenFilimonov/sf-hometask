package com.sunfinance.hometask.api.rest.verification;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sunfinance.hometask.api.VerificationSubject;

import java.io.IOException;

public class SubjectSerializer extends JsonSerializer<VerificationSubject> {

    @Override
    public void serialize(VerificationSubject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("identity", value.getIdentity().getVal());
        gen.writeStringField("type", value.getType().getVal());
        gen.writeEndObject();
    }
}
