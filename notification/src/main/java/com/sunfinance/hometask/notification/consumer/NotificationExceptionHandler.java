package com.sunfinance.hometask.notification.consumer;

import com.sunfinance.hometask.api.error.ErrorCode;
import com.sunfinance.hometask.api.error.ResultError;
import com.sunfinance.hometask.api.event.notification.NotificationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationExceptionHandler {
    public NotificationResult handle(Exception e) {
        //TODO implement: resolve exceptions by types, write corresponding error code
        log.error(e.getMessage(), e);
        return NotificationResult.builder()
                                 .error(ResultError.builder()
                                                   .code(ErrorCode.INTERNAL_ERROR)
                                                   .message(e.getMessage())
                                                   .build())
                                 .build();
    }
}
