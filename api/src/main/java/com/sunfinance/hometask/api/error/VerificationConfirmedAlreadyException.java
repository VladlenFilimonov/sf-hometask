package com.sunfinance.hometask.api.error;

public class VerificationConfirmedAlreadyException extends RuntimeException {

    public VerificationConfirmedAlreadyException() {
    }

    public VerificationConfirmedAlreadyException(String message) {
        super(message);
    }
}
