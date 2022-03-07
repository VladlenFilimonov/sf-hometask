package com.sunfinance.hometask.verification.core.aggregate;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserInfo {

    String ip;
    String agent;

}
