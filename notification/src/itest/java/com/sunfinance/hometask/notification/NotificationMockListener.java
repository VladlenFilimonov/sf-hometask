package com.sunfinance.hometask.notification;

import com.sunfinance.hometask.api.event.Topic;
import lombok.Getter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Getter
@Component
public class NotificationMockListener {

    private String createdEvent;
    private String dispatcherEvent;

    @KafkaListener(topics = Topic.NOTIFICATION_CREATED, groupId = "notification")
    public void listenCreatedEvent(String event) {
        createdEvent = event;
    }

    @KafkaListener(topics = Topic.NOTIFICATION_DISPATCHED, groupId = "notification")
    public void listenConfirmEvent(String event) {
        dispatcherEvent = event;
    }

}
