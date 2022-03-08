package com.sunfinance.hometask.verification.core.rules;

import com.sunfinance.hometask.api.error.VerificationMaxAttemptsException;
import com.sunfinance.hometask.api.error.VerificationNotFoundException;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationEvent;
import com.sunfinance.hometask.verification.core.config.properties.VerificationProperties;
import com.sunfinance.hometask.verification.core.persistence.VerificationEntity;
import com.sunfinance.hometask.verification.core.persistence.VerificationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MaxAttemptsVerificationConfirmationRuleTest {


    private final static UUID VERIFICATION_ID = UUID.fromString("8633e6f8-3b48-4130-af61-28a39bd58b34");
    private final static int CONFIRMATION_ATTEMPTS_ENOUGH = 1;
    private final static int CONFIRMATION_ATTEMPTS_NOT_ENOUGH = 5;

    @Mock
    private VerificationRepository repository;
    @Mock
    private VerificationProperties properties;

    @InjectMocks
    private MaxAttemptsVerificationConfirmationRule victim;

    @Test
    public void shouldPassMaxAttemptValidationSuccessfully() {
        var persistedEntity = prepareEntity(CONFIRMATION_ATTEMPTS_ENOUGH);
        var event = prepareEvent();
        when(repository.findByVerificationId(VERIFICATION_ID.toString()))
                .thenReturn(Optional.of(persistedEntity));
        when(properties.getMaxVerificationAttempts())
                .thenReturn(5);

        victim.apply(event);
    }

    @Test(expected = VerificationMaxAttemptsException.class)
    public void shouldThrowMaxAttemptsException() {
        var persistedEntity = prepareEntity(CONFIRMATION_ATTEMPTS_NOT_ENOUGH);
        var event = prepareEvent();
        when(repository.findByVerificationId(VERIFICATION_ID.toString()))
                .thenReturn(Optional.of(persistedEntity));
        when(properties.getMaxVerificationAttempts())
                .thenReturn(5);

        victim.apply(event);
    }

    @Test(expected = VerificationNotFoundException.class)
    public void shouldThrowNotFoundException_whenVerificationNotFound() {
        var verificationConfirmationEvent = prepareEvent();
        when(repository.findByVerificationId(VERIFICATION_ID.toString()))
                .thenReturn(Optional.empty());
        victim.apply(verificationConfirmationEvent);
    }

    private VerificationEntity prepareEntity(int attempts) {
        return VerificationEntity.builder()
                                 .id(1L)
                                 .verificationId(VERIFICATION_ID.toString())
                                 .attempts(attempts)
                                 .build();
    }

    private VerificationConfirmationEvent prepareEvent() {
        return VerificationConfirmationEvent.builder()
                                            .verificationId(VERIFICATION_ID)
                                            .build();
    }

}