package com.sunfinance.hometask.api.error;

public enum ErrorCode {
    MALFORMED_JSON_PASSED("Malformed JSON passed"),
    DUPLICATED_VERIFICATION("Duplicated verification"),
    INVALID_SUBJECT("Validation failed: invalid subject supplied"),
    NO_PERMISSIONS("No permissions to confirm verification"),
    VERIFICATION_NOT_FOUND("Verification not found"),
    VERIFICATION_EXPIRED("Verification expired"),
    INVALID_VERIFICATION_CODE("Validation failed: invalid code supplied"),
    VALIDATION_EXCEPTION("Validation exception"),
    INTERNAL_ERROR("Internal error");

    private final String description;

    ErrorCode(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
