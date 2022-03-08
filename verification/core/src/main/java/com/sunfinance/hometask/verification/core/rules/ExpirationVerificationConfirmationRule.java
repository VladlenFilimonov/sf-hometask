package com.sunfinance.hometask.verification.core.rules;

import com.sunfinance.hometask.api.error.VerificationExpiredException;
import com.sunfinance.hometask.api.error.VerificationNotFoundException;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationEvent;
import com.sunfinance.hometask.verification.core.config.properties.VerificationProperties;
import com.sunfinance.hometask.verification.core.persistence.VerificationEntity;
import com.sunfinance.hometask.verification.core.persistence.VerificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Optional;

import static com.sunfinance.hometask.verification.core.rules.BusinessRuleOrders.EXPIRATION_VERIFICATION_ORDER;

@Component
@AllArgsConstructor
public class ExpirationVerificationConfirmationRule implements BusinessRule<VerificationConfirmationEvent> {

    private final Clock clock;
    private final VerificationRepository repository;
    private final VerificationProperties properties;

    @Override
    public Optional<VerificationConfirmationEvent> apply(VerificationConfirmationEvent event) {
        return repository.findByVerificationId(event.getVerificationId().toString())
                         .map(entity -> checkForVerificationExpired(entity, event))
                         .or(this::throwVerificationNotFoundException);
    }

    @Override
    public int getOrder() {
        return EXPIRATION_VERIFICATION_ORDER;
    }

    private VerificationConfirmationEvent checkForVerificationExpired(VerificationEntity entity, VerificationConfirmationEvent event) {
        return Optional.of(extractExpirationTime(entity))
                       .filter(this::isNotExpired)
                       .map(e -> event)
                       .orElseThrow(() -> new VerificationExpiredException("Verification expired; id " + entity.getVerificationId()));
    }

    private boolean isNotExpired(ZonedDateTime expirationTime) {
        return !getCurrentTime().isAfter(expirationTime);
    }

    private ZonedDateTime getCurrentTime() {
        return clock.instant()
                    .atZone(clock.getZone());
    }

    private ZonedDateTime extractExpirationTime(VerificationEntity entity) {
        return entity.getCreatedAt()
                     .atZone(clock.getZone())
                     .plusSeconds(properties.getTtl().toSeconds());
    }

    private Optional<VerificationConfirmationEvent> throwVerificationNotFoundException() {
        throw new VerificationNotFoundException("No verification found");
    }
}
