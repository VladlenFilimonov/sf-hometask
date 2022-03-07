package com.sunfinance.hometask.verification.core.consumer;

public interface VerificationExceptionHandler<R> {

    R handle(Throwable ex);

}
