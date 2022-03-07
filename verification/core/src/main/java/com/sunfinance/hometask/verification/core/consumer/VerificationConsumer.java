package com.sunfinance.hometask.verification.core.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationEvent;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationResult;
import com.sunfinance.hometask.api.event.verification.VerificationCreateEvent;
import com.sunfinance.hometask.api.event.verification.VerificationCreateResult;
import com.sunfinance.hometask.verification.core.service.VerificationService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.sunfinance.hometask.api.event.Topic.VERIFICATION_CONFIRMATION_REQUEST;
import static com.sunfinance.hometask.api.event.Topic.VERIFICATION_CREATE_REQUEST;

@Service
@AllArgsConstructor
public class VerificationConsumer {

    private final ObjectMapper objectMapper;
    private final VerificationService service;
    private final VerificationExceptionHandler<VerificationCreateResult> createExceptionsHandler;
    private final VerificationExceptionHandler<VerificationConfirmationResult> confirmVerificationExceptionHandler;

    @KafkaListener(topics = VERIFICATION_CREATE_REQUEST, groupId = "verification")
    @SendTo
    public String listenCreate(String string) {
        try {
            return Optional.of(toObject(string, VerificationCreateEvent.class))
                           .flatMap(service::create)
                           .map(this::toJson)
                           .orElseThrow(() -> new RuntimeException("No response from service on create"));
        } catch (Exception e) {
            var result = createExceptionsHandler.handle(e);
            return toJson(result);
        }
    }

    @KafkaListener(topics = VERIFICATION_CONFIRMATION_REQUEST, groupId = "verification")
    @SendTo
    public String listenConfirm(String string) {
        try {
            return Optional.of(toObject(string, VerificationConfirmationEvent.class))
                           .flatMap(service::confirm)
                           .map(this::toJson)
                           .orElseThrow(() -> new RuntimeException("No response from service on confirm"));
        } catch (Exception e) {
            var result = confirmVerificationExceptionHandler.handle(e);
            return toJson(result);
        }
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
