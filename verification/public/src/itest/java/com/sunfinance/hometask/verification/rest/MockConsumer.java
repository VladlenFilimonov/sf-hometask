package com.sunfinance.hometask.verification.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunfinance.hometask.api.SubjectIdentity;
import com.sunfinance.hometask.api.SubjectType;
import com.sunfinance.hometask.api.UserInfo;
import com.sunfinance.hometask.api.VerificationSubject;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationEvent;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationResult;
import com.sunfinance.hometask.api.event.verification.VerificationCreateEvent;
import com.sunfinance.hometask.api.event.verification.VerificationCreateResult;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.UUID;

import static com.sunfinance.hometask.api.event.Topic.VERIFICATION_CONFIRMATION_REQUEST;
import static com.sunfinance.hometask.api.event.Topic.VERIFICATION_CREATE_REQUEST;
import static org.junit.Assert.assertEquals;

@Service
@AllArgsConstructor
public class MockConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = VERIFICATION_CREATE_REQUEST, groupId = "verification")
    @SendTo
    public String listenCreate(String string) {
        var event = toObject(string, VerificationCreateEvent.class);
        var expectedEvent = VerificationCreateEvent.builder()
                                                   .subject(VerificationSubject.builder()
                                                                               .type(SubjectType.EMAIL_CONFIRMATION)
                                                                               .identity(SubjectIdentity.build(SubjectType.EMAIL_CONFIRMATION, "john.doe@abc.xyz"))
                                                                               .build())
                                                   .userInfo(UserInfo.builder()
                                                                     .ipAddress("127.0.0.1")
                                                                     .userAgent("test-agent")
                                                                     .build())
                                                   .build();
        assertEquals(expectedEvent, event);
        return toJson(VerificationCreateResult.builder()
                                              .id(UUID.fromString("8633e6f8-3b48-4130-af61-28a39bd58b34"))
                                              .build());
    }

    @KafkaListener(topics = VERIFICATION_CONFIRMATION_REQUEST, groupId = "verification")
    @SendTo
    public String listenConfirm(String string) {
        var event = toObject(string, VerificationConfirmationEvent.class);
        var expectedEvent = VerificationConfirmationEvent.builder()
                                                         .code(BigInteger.valueOf(1234))
                                                         .userInfo(UserInfo.builder()
                                                                           .ipAddress("127.0.0.1")
                                                                           .userAgent("test-agent")
                                                                           .build())
                                                         .verificationId(UUID.fromString("8633e6f8-3b48-4130-af61-28a39bd58b34"))
                                                         .build();
        assertEquals(expectedEvent, event);
        return toJson(VerificationConfirmationResult.builder()
                                                    .id(UUID.fromString("8633e6f8-3b48-4130-af61-28a39bd58b34"))
                                                    .build());

    }

    private <T> T toObject(String string, Class<T> c) {
        try {
            return objectMapper.readValue(string, c);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String toJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
