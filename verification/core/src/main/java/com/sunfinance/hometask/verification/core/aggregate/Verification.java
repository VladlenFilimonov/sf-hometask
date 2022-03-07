package com.sunfinance.hometask.verification.core.aggregate;

import lombok.Builder;
import lombok.Value;

import java.math.BigInteger;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Verification {

    UUID id;
    boolean confirmed;
    BigInteger code;
    Subject subject;
    UserInfo userInfo;

}
