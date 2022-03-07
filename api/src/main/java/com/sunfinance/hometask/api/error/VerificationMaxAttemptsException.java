package com.sunfinance.hometask.api.error;

public class VerificationMaxAttemptsException extends RuntimeException {

    public VerificationMaxAttemptsException() {
        super();
    }

    public VerificationMaxAttemptsException(String message) {
        super(message);
    }
}
