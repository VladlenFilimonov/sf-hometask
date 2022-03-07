package com.sunfinance.hometask.event.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunfinance.hometask.event.EventService;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class KafkaEventService implements EventService {

    private final ReplyingKafkaTemplate<String, String, String> replyingTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public <Event, Result> CompletableFuture<Result> sendAndReceive(Event event,
                                                                    String topic,
                                                                    String replyTopic,
                                                                    Class<Result> resultClass) {
        return CompletableFuture.supplyAsync(() -> new ProducerRecord<String, String>(topic, toJson(event)))
                                .thenApply(request -> addReplyHeader(request, replyTopic))
                                .thenCompose(request -> replyingTemplate.sendAndReceive(request).completable())
                                .thenApply(reply -> toResult(reply.value(), resultClass));
    }

    private ProducerRecord<String, String> addReplyHeader(ProducerRecord<String, String> request,
                                                          String replyTopic) {
        request.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes()));
        return request;
    }

    @Override
    public <Event> void send(Event createdEvent, String topic) {
        var data = toJson(createdEvent);
        replyingTemplate.send(topic, data);
    }

    private <Result> Result toResult(String value, Class<Result> resultClass) {
        try {
            return objectMapper.readValue(value, resultClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Deserialization failed", e);
        }
    }

    private String toJson(Object o) {
        try {
            var s = objectMapper.writeValueAsString(o);
            return s;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Serialization failed", e);
        }
    }
}
