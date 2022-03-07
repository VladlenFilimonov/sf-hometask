package com.sunfinance.hometask.api.error;

public class NoPermissionsException extends RuntimeException {

    public NoPermissionsException() {
    }

    public NoPermissionsException(String message) {
        super(message);
    }
}
