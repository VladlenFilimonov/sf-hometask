package com.sunfinance.hometask.verification.core.rules;

import com.sunfinance.hometask.api.error.VerificationConfirmedAlreadyException;
import com.sunfinance.hometask.api.error.VerificationNotFoundException;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationEvent;
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
public class IdempotentVerificationConfirmationRuleTest {

    private final static UUID VERIFICATION_ID = UUID.fromString("8633e6f8-3b48-4130-af61-28a39bd58b34");

    @Mock
    private VerificationRepository repository;

    @InjectMocks
    private IdempotentVerificationConfirmationRule victim;

    @Test
    public void shouldPassIdempotentConfirmationSuccessfully() {
        var verificationConfirmationEvent = prepareEvent();
        var persistedVerificationEntity = prepareVerificationEntity(false);
        when(repository.findByVerificationId(VERIFICATION_ID.toString()))
                .thenReturn(Optional.of(persistedVerificationEntity));
        victim.apply(verificationConfirmationEvent);
    }

    @Test(expected = VerificationConfirmedAlreadyException.class)
    public void shouldThrowIdempotentException_whenVerificationConfirmedAlready() {
        var verificationConfirmationEvent = prepareEvent();
        var persistedVerificationEntity = prepareVerificationEntity(true);
        when(repository.findByVerificationId(VERIFICATION_ID.toString()))
                .thenReturn(Optional.of(persistedVerificationEntity));
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

    private VerificationEntity prepareVerificationEntity(boolean confirmed) {
        return VerificationEntity.builder()
                                 .id(1L)
                                 .verificationId(VERIFICATION_ID.toString())
                                 .confirmed(confirmed)
                                 .build();
    }
}