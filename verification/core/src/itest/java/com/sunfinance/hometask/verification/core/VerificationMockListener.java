package com.sunfinance.hometask.verification.core;

import com.sunfinance.hometask.api.event.Topic;
import lombok.Getter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Getter
@Component
public class VerificationMockListener {

    private String createdEvent;
    private String confirmedEvent;
    private String confirmationFailedEvent;

    @KafkaListener(topics = Topic.VERIFICATION_CREATED, groupId = "verification")
    public void listenCreatedEvent(String event) {
        createdEvent = event;
    }

    @KafkaListener(topics = Topic.VERIFICATION_CONFIRMED, groupId = "verification")
    public void listenConfirmEvent(String event) {
        confirmedEvent = event;
    }

    @KafkaListener(topics = Topic.VERIFICATION_CONFIRMATION_FAILED, groupId = "verification")
    public void listenConfirmFailedEvent(String event) {
        confirmationFailedEvent = event;
    }
}
