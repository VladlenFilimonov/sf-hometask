package com.sunfinance.hometask.api;

public interface SubjectIdentity {

    static SubjectIdentity build(SubjectType type, String identityValue) {
        switch (type) {
            case MOBILE_CONFIRMATION:
                return PhoneNumberIdentity.builder()
                                          .val(identityValue)
                                          .build();
            case EMAIL_CONFIRMATION:
                return EmailIdentity.builder()
                                    .val(identityValue)
                                    .build();
        }
        throw new RuntimeException("No such identity found");
    }

    String getVal();

}
