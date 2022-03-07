package com.sunfinance.hometask.verification.core.aggregate;

import com.sunfinance.hometask.api.SubjectType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Subject {

    String identity;
    SubjectType type;

}
