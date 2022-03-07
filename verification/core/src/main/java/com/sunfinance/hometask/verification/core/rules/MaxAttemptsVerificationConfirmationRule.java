package com.sunfinance.hometask.verification.core.rules;

import com.sunfinance.hometask.api.error.VerificationMaxAttemptsException;
import com.sunfinance.hometask.api.error.VerificationNotFoundException;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationEvent;
import com.sunfinance.hometask.verification.core.config.properties.VerificationProperties;
import com.sunfinance.hometask.verification.core.persistance.VerificationEntity;
import com.sunfinance.hometask.verification.core.persistance.VerificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.sunfinance.hometask.verification.core.rules.BusinessRuleOrders.MAX_ATTEMPTS_ORDER;

@Component
@AllArgsConstructor
public class MaxAttemptsVerificationConfirmationRule implements BusinessRule<VerificationConfirmationEvent> {

    private final VerificationRepository repository;
    private final VerificationProperties properties;

    @Override
    public Optional<VerificationConfirmationEvent> apply(VerificationConfirmationEvent event) {
        return repository.findByVerificationId(event.getVerificationId().toString())
                         .map(this::checkForFreeAttempts)
                         .map(e -> event)
                         .or(this::throwVerificationNotFoundException);
    }

    @Override
    public int getOrder() {
        return MAX_ATTEMPTS_ORDER;
    }

    private VerificationEntity checkForFreeAttempts(VerificationEntity entity) {
        return Optional.of(entity)
                       .filter(this::hasFreeAttempts)
                       .orElseThrow(() -> new VerificationMaxAttemptsException(
                               "Maximum attempts to confirm verification - " + properties.getMaxVerificationAttempts()));
    }

    private boolean hasFreeAttempts(VerificationEntity entity) {
        return entity.getAttempts() < properties.getMaxVerificationAttempts();
    }

    private Optional<VerificationConfirmationEvent> throwVerificationNotFoundException() {
        throw new VerificationNotFoundException("No verification found");
    }
}
