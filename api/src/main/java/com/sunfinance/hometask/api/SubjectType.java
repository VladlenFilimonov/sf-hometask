package com.sunfinance.hometask.api;

public enum SubjectType {

    MOBILE_CONFIRMATION(SubjectTypeConstant.MOBILE_CONFIRMATION_NAME),
    EMAIL_CONFIRMATION(SubjectTypeConstant.EMAIL_CONFIRMATION_NAME);

    String val;

    SubjectType(String type) {
        this.val = type;
    }

    public String getVal() {
        return val;
    }
}
