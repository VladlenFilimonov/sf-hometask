package com.sunfinance.hometask.verification.core.rules;

import com.sunfinance.hometask.api.VerificationSubject;
import com.sunfinance.hometask.api.error.DuplicatedVerificationException;
import com.sunfinance.hometask.api.event.verification.VerificationCreateEvent;
import com.sunfinance.hometask.verification.core.cache.CacheService;
import com.sunfinance.hometask.verification.core.config.properties.VerificationProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class DuplicateVerificationCreationRule implements BusinessRule<VerificationCreateEvent> {

    private final static String KEY_PREFIX = "verification:create:";

    private final VerificationProperties verificationProperties;
    private final CacheService verificationCache;

    @Override
    public Optional<VerificationCreateEvent> apply(VerificationCreateEvent event) {
        return Optional.of(prepareKey(event.getSubject()))
                       .map(key -> verificationCache.saveIfAbsent(key, event, verificationProperties.getTtl()))
                       .filter(isCached -> isCached)
                       .map(e -> event)
                       .or(() -> throwDuplicateVerificationException(event));
    }

    private Optional<VerificationCreateEvent> throwDuplicateVerificationException(VerificationCreateEvent event) {
        throw new DuplicatedVerificationException("Verification created already for subject : " + event.getSubject());
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private String prepareKey(VerificationSubject subject) {
        return KEY_PREFIX + subject.hashCode();
    }
}
