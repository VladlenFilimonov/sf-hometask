package com.sunfinance.hometask.event;

import java.util.concurrent.CompletableFuture;

public interface EventService {

    <Event, Result> CompletableFuture<Result> sendAndReceive(Event event, String topic, String replyTopic, Class<Result> resultClass);

    <Event> void send(Event createdEvent, String topic);
}
