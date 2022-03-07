package com.sunfinance.hometask.verification.core.service.impl;

import com.sunfinance.hometask.api.event.verification.VerificationConfirmationEvent;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationResult;
import com.sunfinance.hometask.api.event.verification.VerificationCreateEvent;
import com.sunfinance.hometask.api.event.verification.VerificationCreateResult;
import com.sunfinance.hometask.verification.core.rules.VerificationConfirmationBusinessRules;
import com.sunfinance.hometask.verification.core.rules.VerificationCreationBusinessRules;
import com.sunfinance.hometask.verification.core.service.VerificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VerificationFacade implements VerificationService {

    private final VerificationCreationBusinessRules creationBusinessRules;
    private final VerificationConfirmationBusinessRules confirmationBusinessRules;
    private final VerificationCreationService verificationCreationService;
    private final VerificationConfirmationService verificationConfirmationService;

    @Override
    public Optional<VerificationCreateResult> create(VerificationCreateEvent verificationCreateEvent) {
        return Optional.of(verificationCreateEvent)
                       .map(this::applyCreationRules)
                       .flatMap(verificationCreationService::create);
    }

    @Override
    public Optional<VerificationConfirmationResult> confirm(VerificationConfirmationEvent verificationConfirmationEvent) {
        return Optional.of(verificationConfirmationEvent)
                       .map(this::applyConfirmationRules)
                       .flatMap(verificationConfirmationService::confirm);
    }

    private VerificationConfirmationEvent applyConfirmationRules(VerificationConfirmationEvent event) {
        confirmationBusinessRules.getRules()
                                 .forEach(rule -> rule.apply(event));
        return event;
    }

    private VerificationCreateEvent applyCreationRules(VerificationCreateEvent event) {
        creationBusinessRules.getRules()
                             .forEach(rule -> rule.apply(event));
        return event;
    }

}
