package com.sunfinance.hometask.verification.rest;

import com.sunfinance.hometask.api.error.ResultError;
import com.sunfinance.hometask.api.event.verification.VerificationCreateResult;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class TestErrorHandler implements KafkaListenerErrorHandler {
    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        return VerificationCreateResult.builder()
                                       .error(ResultError.builder()
                                                         .message(exception.getMessage())
                                                         .build())
                                       .build();
    }
}
