package com.sunfinance.hometask.api.error;

public class VerificationExpiredException extends RuntimeException {

    public VerificationExpiredException() {
    }

    public VerificationExpiredException(String message) {
        super(message);
    }
}
