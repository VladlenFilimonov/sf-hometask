package com.sunfinance.hometask.notification.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunfinance.hometask.api.event.verification.VerificationCreatedEvent;
import com.sunfinance.hometask.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.sunfinance.hometask.api.event.Topic.VERIFICATION_CREATED;

@Service
@AllArgsConstructor
public class NotificationConsumer {

    private final ObjectMapper objectMapper;
    private final NotificationService service;
    private final NotificationExceptionHandler exceptionHandler;

    @KafkaListener(topics = VERIFICATION_CREATED, groupId = "notification")
    @SendTo
    public String listenCreate(String string) {
        try {
            return Optional.of(toObject(string, VerificationCreatedEvent.class))
                           .flatMap(service::send)
                           .map(this::toJson)
                           .orElseThrow(() -> new RuntimeException("No response from service on notification sending"));
        } catch (Exception e) {
            var result = exceptionHandler.handle(e);
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
