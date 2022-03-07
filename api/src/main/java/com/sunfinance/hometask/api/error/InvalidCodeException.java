package com.sunfinance.hometask.api.error;

public class InvalidCodeException extends RuntimeException {
    public InvalidCodeException() {
    }

    public InvalidCodeException(String message) {
        super(message);
    }
}
