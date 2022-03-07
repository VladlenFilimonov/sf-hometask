package com.sunfinance.hometask.api.error;

public class InvalidSubjectException extends RuntimeException {
    public InvalidSubjectException(String message) {
        super(message);
    }

    public InvalidSubjectException(String message, IllegalArgumentException e) {
        super(message, e);
    }
}
