package com.sunfinance.hometask.api.error;

public class VerificationNotFoundException extends RuntimeException {

    public VerificationNotFoundException() {
    }

    public VerificationNotFoundException(String message) {
        super(message);
    }
}
