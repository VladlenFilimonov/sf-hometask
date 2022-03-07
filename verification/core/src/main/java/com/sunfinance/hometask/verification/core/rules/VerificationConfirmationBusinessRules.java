package com.sunfinance.hometask.verification.core.rules;

import com.sunfinance.hometask.api.event.verification.VerificationConfirmationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class VerificationConfirmationBusinessRules {

    private final List<BusinessRule<VerificationConfirmationEvent>> rules;

}
