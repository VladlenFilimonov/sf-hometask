package com.sunfinance.hometask.verification.core.rules;

public class BusinessRuleOrders {

    public static final int IDEMPOTENT_CONFIRMATION_ORDER = Integer.MIN_VALUE;
    public static final int SAME_CLIENT_INFO_ORDER = IDEMPOTENT_CONFIRMATION_ORDER + 10;
    public static final int EXPIRATION_VERIFICATION_ORDER = SAME_CLIENT_INFO_ORDER + 10;
    public static final int MAX_ATTEMPTS_ORDER = EXPIRATION_VERIFICATION_ORDER + 10;
}
