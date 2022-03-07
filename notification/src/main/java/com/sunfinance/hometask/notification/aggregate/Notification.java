package com.sunfinance.hometask.notification.aggregate;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Notification {

    UUID id;
    String recipient;
    String channel;
    String body;
    boolean dispatched;

}
