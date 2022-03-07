package com.sunfinance.hometask.api.error;

public class DuplicatedVerificationException extends RuntimeException {
    public DuplicatedVerificationException(String message) {
        super(message);
    }
}
