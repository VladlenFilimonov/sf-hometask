package com.sunfinance.hometask.verification.core.rules;

import com.sunfinance.hometask.api.event.verification.VerificationCreateEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class VerificationCreationBusinessRules {

    private final List<BusinessRule<VerificationCreateEvent>> rules;

}
