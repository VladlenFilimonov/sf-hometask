package com.sunfinance.hometask.verification.core.service.impl;

import com.sunfinance.hometask.api.error.InvalidCodeException;
import com.sunfinance.hometask.api.event.Topic;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationEvent;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationFailedEvent;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmationResult;
import com.sunfinance.hometask.api.event.verification.VerificationConfirmedEvent;
import com.sunfinance.hometask.event.EventService;
import com.sunfinance.hometask.verification.core.aggregate.Verification;
import com.sunfinance.hometask.verification.core.aggregate.VerificationFactory;
import com.sunfinance.hometask.verification.core.persistence.VerificationEntity;
import com.sunfinance.hometask.verification.core.persistence.VerificationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VerificationConfirmationServiceTest {

    private final static UUID VERIFICATION_ID = UUID.fromString("8633e6f8-3b48-4130-af61-28a39bd58b34");
    private final static BigInteger CODE = BigInteger.valueOf(1234L);
    private final static BigInteger NOT_MATCHED_CODE = BigInteger.valueOf(4321L);

    @Mock
    private EventService eventService;
    @Mock
    private ConversionService conversionService;
    @Mock
    private VerificationFactory verificationFactory;
    @Mock
    private VerificationRepository repository;

    @InjectMocks
    private VerificationConfirmationService victim;

    @Test
    public void shouldConfirmSuccessfully() {
        var confirmationEvent = prepareConfirmationEvent(CODE);
        var verificationEntity = prepareEntity(false, CODE, 0);
        var verification = prepareVerification(false, CODE);
        var confirmedVerification = prepareVerification(true, CODE);
        var confirmedEntity = prepareEntity(true, CODE, 0);
        var confirmedEvent = prepareConfirmedEvent();
        var result = prepareResult();
        when(repository.findByVerificationId(confirmationEvent.getVerificationId().toString()))
                .thenReturn(Optional.of(verificationEntity))
                .thenReturn(Optional.of(verificationEntity));
        when(verificationFactory.create(verificationEntity))
                .thenReturn(verification);
        when(verificationFactory.confirm(verification))
                .thenReturn(confirmedVerification);
        when(repository.save(confirmedEntity))
                .thenReturn(confirmedEntity);
        when(conversionService.convert(confirmedVerification, VerificationConfirmedEvent.class))
                .thenReturn(confirmedEvent);
        when(conversionService.convert(confirmedVerification, VerificationConfirmationResult.class))
                .thenReturn(result);

        var confirmed = victim.confirm(confirmationEvent)
                              .orElseThrow();
        assertEquals(VERIFICATION_ID, confirmed.getId());

        verify(eventService, times(1)).send(confirmedEvent, Topic.VERIFICATION_CONFIRMED);

    }

    @Test(expected = InvalidCodeException.class)
    public void shouldIncrementAttempt() {
        var confirmationEvent = prepareConfirmationEvent(CODE);
        var verificationEntity = prepareEntity(false, NOT_MATCHED_CODE, 0);
        var verification = prepareVerification(false, NOT_MATCHED_CODE);
        var incrementedAttemptEntity = prepareEntity(false, NOT_MATCHED_CODE, 1);
        var confirmationFailedEvent = prepareFailedEvent();
        var result = prepareResult();

        when(repository.findByVerificationId(confirmationEvent.getVerificationId().toString()))
                .thenReturn(Optional.of(verificationEntity))
                .thenReturn(Optional.of(verificationEntity));
        when(verificationFactory.create(verificationEntity))
                .thenReturn(verification);
        when(repository.save(incrementedAttemptEntity))
                .thenReturn(incrementedAttemptEntity);
        when(conversionService.convert(verification, VerificationConfirmationFailedEvent.class))
                .thenReturn(confirmationFailedEvent);

        var confirmed = victim.confirm(confirmationEvent)
                              .orElseThrow();
        assertEquals(VERIFICATION_ID, confirmed.getId());

        verify(eventService, times(1)).send(confirmationFailedEvent, Topic.VERIFICATION_CONFIRMATION_FAILED);
    }

    private VerificationConfirmationFailedEvent prepareFailedEvent() {
        return VerificationConfirmationFailedEvent.builder()
                                                  .build();
    }

    private VerificationConfirmationResult prepareResult() {
        return VerificationConfirmationResult.builder()
                                             .id(VERIFICATION_ID)
                                             .build();
    }

    private VerificationConfirmedEvent prepareConfirmedEvent() {
        return VerificationConfirmedEvent.builder()
                                         .build();
    }

    private VerificationEntity prepareEntity(boolean confirmed, BigInteger code, int attempt) {
        return VerificationEntity.builder()
                                 .id(1L)
                                 .confirmed(confirmed)
                                 .code(code)
                                 .verificationId(VERIFICATION_ID.toString())
                                 .attempts(attempt)
                                 .build();
    }

    private VerificationConfirmationEvent prepareConfirmationEvent(BigInteger code) {
        return VerificationConfirmationEvent.builder()
                                            .verificationId(VERIFICATION_ID)
                                            .code(code)
                                            .build();
    }

    private Verification prepareVerification(boolean confirmed, BigInteger code) {
        return Verification.builder()
                           .id(VERIFICATION_ID)
                           .code(code)
                           .confirmed(confirmed)
                           .build();
    }


}