package com.sunfinance.hometask.verification.core.rules;

import com.sunfinance.hometask.api.error.VerificationExpiredException;
import com.sunfinance.hometask.api.error.VerificationNotFoundException;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationEvent;
import com.sunfinance.hometask.verification.core.config.properties.VerificationProperties;
import com.sunfinance.hometask.verification.core.persistence.VerificationEntity;
import com.sunfinance.hometask.verification.core.persistence.VerificationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExpirationVerificationConfirmationRuleTest {

    private final static UUID VERIFICATION_ID = UUID.fromString("8633e6f8-3b48-4130-af61-28a39bd58b34");
    private final static Instant CREATED_AT = Instant.parse("2022-01-01T10:06:10.00Z");
    private final static Instant CREATED_AT_EXPIRED = Instant.parse("2022-01-01T10:04:10.00Z");
    private final static Long TTL_MINUTES = 5L;

    private final Clock clock = Clock.fixed(Instant.parse("2022-01-01T10:10:10.00Z"), ZoneId.of("UTC"));

    @Mock
    private VerificationRepository repository;
    @Mock
    private VerificationProperties properties;

    private ExpirationVerificationConfirmationRule victim;

    @Before
    public void setUp() {
        victim = new ExpirationVerificationConfirmationRule(clock, repository, properties);
    }

    @Test
    public void shouldPassExpirationValidationSuccessfully() {
        var verificationConfirmationEvent = prepareEvent();
        var persistedVerificationEntity = prepareVerificationEntity(CREATED_AT);
        when(repository.findByVerificationId(VERIFICATION_ID.toString()))
                .thenReturn(Optional.of(persistedVerificationEntity));
        when(properties.getTtl())
                .thenReturn(Duration.ofMinutes(TTL_MINUTES));
        victim.apply(verificationConfirmationEvent);
    }

    @Test(expected = VerificationExpiredException.class)
    public void shouldThrowExpirationException_whenVerificationExpired() {
        var verificationConfirmationEvent = prepareEvent();
        var persistedVerificationEntity = prepareVerificationEntity(CREATED_AT_EXPIRED);
        when(repository.findByVerificationId(VERIFICATION_ID.toString()))
                .thenReturn(Optional.of(persistedVerificationEntity));
        when(properties.getTtl())
                .thenReturn(Duration.ofMinutes(TTL_MINUTES));
        victim.apply(verificationConfirmationEvent);
    }

    @Test(expected = VerificationNotFoundException.class)
    public void shouldThrowNotFoundException_whenVerificationNotFound() {
        var verificationConfirmationEvent = prepareEvent();
        when(repository.findByVerificationId(VERIFICATION_ID.toString()))
                .thenReturn(Optional.empty());
        victim.apply(verificationConfirmationEvent);
    }

    private VerificationConfirmationEvent prepareEvent() {
        return VerificationConfirmationEvent.builder()
                                            .verificationId(VERIFICATION_ID)
                                            .build();
    }

    private VerificationEntity prepareVerificationEntity(Instant createdAt) {
        return VerificationEntity.builder()
                                 .id(1L)
                                 .verificationId(VERIFICATION_ID.toString())
                                 .createdAt(createdAt)
                                 .build();
    }

}
