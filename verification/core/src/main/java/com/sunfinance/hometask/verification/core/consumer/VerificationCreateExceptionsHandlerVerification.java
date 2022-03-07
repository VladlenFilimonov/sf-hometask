package com.sunfinance.hometask.verification.core.consumer;

import com.sunfinance.hometask.api.error.ErrorCode;
import com.sunfinance.hometask.api.error.ResultError;
import com.sunfinance.hometask.api.event.verification.VerificationCreateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VerificationCreateExceptionsHandlerVerification implements VerificationExceptionHandler<VerificationCreateResult> {

    @Override
    public VerificationCreateResult handle(Throwable ex) {
        //TODO implement: resolve exceptions by types, write corresponding error code
        log.error(ex.getMessage(), ex);
        return VerificationCreateResult.builder()
                                       .error(ResultError.builder()
                                                         .code(ErrorCode.INTERNAL_ERROR)
                                                         .message(ex.getMessage())
                                                         .build())
                                       .build();
    }
}
