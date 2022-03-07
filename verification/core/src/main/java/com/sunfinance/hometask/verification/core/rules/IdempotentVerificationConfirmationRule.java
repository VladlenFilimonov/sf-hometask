package com.sunfinance.hometask.verification.core.rules;

import com.sunfinance.hometask.api.error.VerificationConfirmedAlreadyException;
import com.sunfinance.hometask.api.error.VerificationNotFoundException;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationEvent;
import com.sunfinance.hometask.verification.core.persistance.VerificationEntity;
import com.sunfinance.hometask.verification.core.persistance.VerificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.sunfinance.hometask.verification.core.rules.BusinessRuleOrders.IDEMPOTENT_CONFIRMATION_ORDER;

@Component
@AllArgsConstructor
public class IdempotentVerificationConfirmationRule implements BusinessRule<VerificationConfirmationEvent> {

    private final VerificationRepository repository;

    @Override
    public Optional<VerificationConfirmationEvent> apply(VerificationConfirmationEvent event) {
        return repository.findByVerificationId(event.getVerificationId().toString())
                         .map(this::checkForConfirmationIsNeeded)
                         .map(e -> event)
                         .or(this::throwVerificationNotFoundException);
    }

    private VerificationEntity checkForConfirmationIsNeeded(VerificationEntity entity) {
        return Optional.of(entity)
                       .filter(this::isNotConfirmed)
                       .orElseThrow(() -> new VerificationConfirmedAlreadyException(
                               "Verification confirmed already; id: " + entity.getVerificationId().toString()));
    }

    private boolean isNotConfirmed(VerificationEntity entity) {
        return !entity.isConfirmed();
    }

    @Override
    public int getOrder() {
        return IDEMPOTENT_CONFIRMATION_ORDER;
    }

    private Optional<VerificationConfirmationEvent> throwVerificationNotFoundException() {
        throw new VerificationNotFoundException("No verification found");
    }
}
