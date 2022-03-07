package com.sunfinance.hometask.verification.core.rules;

import java.util.Optional;

public interface BusinessRule<T> {

    Optional<T> apply(T event);

    int getOrder();
}
