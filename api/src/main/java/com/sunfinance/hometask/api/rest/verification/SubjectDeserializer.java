package com.sunfinance.hometask.api.rest.verification;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.sunfinance.hometask.api.EmailIdentity;
import com.sunfinance.hometask.api.PhoneNumberIdentity;
import com.sunfinance.hometask.api.SubjectIdentity;
import com.sunfinance.hometask.api.SubjectType;
import com.sunfinance.hometask.api.VerificationSubject;
import com.sunfinance.hometask.api.error.InvalidSubjectException;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.Optional;

public class SubjectDeserializer extends JsonDeserializer<VerificationSubject> {

    @Override
    public VerificationSubject deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        try {
            return buildVerificationSubject(jp);
        } catch (IllegalArgumentException e) {
            throw new InvalidSubjectException("Invalid subject", e);
        }
    }

    private VerificationSubject buildVerificationSubject(JsonParser jp) throws IOException {
        var node = (JsonNode) jp.getCodec().readTree(jp);
        var type = parseType(node);
        var identity = parseIdentity(node, type);
        return VerificationSubject.builder()
                                  .identity(identity)
                                  .type(type)
                                  .build();
    }

    @Validated
    private SubjectIdentity parseIdentity(JsonNode node, SubjectType type) {
        var identityNode = node.get("identity").asText();
        switch (type) {
            case MOBILE_CONFIRMATION:
                return PhoneNumberIdentity.builder()
                                          .val(identityNode)
                                          .build();
            case EMAIL_CONFIRMATION:
                return EmailIdentity.builder()
                                    .val(identityNode)
                                    .build();
        }
        throw new InvalidSubjectException("Invalid subject");
    }

    private SubjectType parseType(JsonNode node) {
        return Optional.ofNullable(node.get("type"))
                       .map(JsonNode::asText)
                       .map(String::toUpperCase)
                       .map(SubjectType::valueOf)
                       .orElseThrow(() -> new RuntimeException("No such type found"));
    }
}
