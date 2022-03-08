package com.sunfinance.hometask.verification.core.rules;

import com.sunfinance.hometask.api.error.NoPermissionsException;
import com.sunfinance.hometask.api.error.VerificationNotFoundException;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationEvent;
import com.sunfinance.hometask.verification.core.persistence.VerificationEntity;
import com.sunfinance.hometask.verification.core.persistence.VerificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.sunfinance.hometask.verification.core.rules.BusinessRuleOrders.SAME_CLIENT_INFO_ORDER;

@Component
@AllArgsConstructor
public class SameClientInfoVerificationConfirmationRule implements BusinessRule<VerificationConfirmationEvent> {

    private final VerificationRepository repository;

    @Override
    public Optional<VerificationConfirmationEvent> apply(VerificationConfirmationEvent event) {
        return repository.findByVerificationId(event.getVerificationId().toString())
                         .map(entity -> compareClientInfo(entity, event))
                         .map(e -> event)
                         .or(this::throwVerificationNotFoundException);
    }

    private VerificationEntity compareClientInfo(VerificationEntity verificationEntity, VerificationConfirmationEvent event) {
        return Optional.of(verificationEntity)
                       .filter(entity -> compareClientIp(entity, event))
                       .filter(entity -> compareUserAgent(entity, event))
                       .orElseThrow(() -> new NoPermissionsException("Client information is not the same"));
    }

    @Override
    public int getOrder() {
        return SAME_CLIENT_INFO_ORDER;
    }

    private boolean compareUserAgent(VerificationEntity entity,
                                     VerificationConfirmationEvent event) {

        return entity.getClientAgent().equals(event.getUserInfo().getUserAgent());
    }

    private boolean compareClientIp(VerificationEntity entity,
                                    VerificationConfirmationEvent event) {

        return entity.getClientIp().equals(event.getUserInfo().getIpAddress());
    }

    private Optional<VerificationConfirmationEvent> throwVerificationNotFoundException() {
        throw new VerificationNotFoundException("No verification found");
    }
}
