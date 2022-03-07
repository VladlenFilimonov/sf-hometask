package com.sunfinance.hometask.notification.aggregate;

import com.sunfinance.hometask.api.event.verification.VerificationCreatedEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotificationFactory {
    public Notification create(VerificationCreatedEvent event) {
        return Notification.builder()
                           .id(event.getId())
                           .dispatched(false)
                           .channel(event.getSubject().getType().getVal())
                           .recipient(event.getSubject().getIdentity().getVal())
                           .build();
    }

    public Notification setBody(Notification notification, String body) {
        return notification.toBuilder()
                           .body(body)
                           .build();
    }

    public Notification dispatch(Notification notification) {
        return notification.toBuilder()
                           .dispatched(true)
                           .build();
    }
}
