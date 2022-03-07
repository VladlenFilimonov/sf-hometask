package com.sunfinance.hometask.api.event.notification;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class NotificationCreatedEvent {

    UUID id;
    String recipient;

}
