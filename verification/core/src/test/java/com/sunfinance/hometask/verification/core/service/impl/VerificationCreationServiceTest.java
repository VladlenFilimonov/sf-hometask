package com.sunfinance.hometask.verification.core.service.impl;

import com.sunfinance.hometask.api.event.Topic;
import com.sunfinance.hometask.api.event.verification.VerificationCreateEvent;
import com.sunfinance.hometask.api.event.verification.VerificationCreatedEvent;
import com.sunfinance.hometask.event.EventService;
import com.sunfinance.hometask.verification.core.aggregate.Verification;
import com.sunfinance.hometask.verification.core.aggregate.VerificationFactory;
import com.sunfinance.hometask.verification.core.persistence.VerificationEntity;
import com.sunfinance.hometask.verification.core.persistence.VerificationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;

import java.time.Clock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VerificationCreationServiceTest {

    private final Clock clock = Clock.systemUTC();
    @Mock
    private ConversionService conversionService;
    @Mock
    private VerificationFactory factory;
    @Mock
    private VerificationRepository repository;
    @Mock
    private EventService eventService;
    private VerificationCreationService victim;

    @Before
    public void setUp() {
        victim = new VerificationCreationService(clock, conversionService, factory, repository, eventService);
    }

    @Test
    public void shouldCreateVerificationSuccessfully() {
        var event = prepareEvent();
        var verification = prepareVerification();
        var verificationEntity = prepareEntity();
        var verificationCreatedEvent = prepareCreatedEvent();

        when(factory.create(event)).thenReturn(verification);
        when(conversionService.convert(verification, VerificationEntity.class)).thenReturn(verificationEntity);
        when(repository.save(verificationEntity)).thenReturn(verificationEntity);
        when(conversionService.convert(verification, VerificationCreatedEvent.class)).thenReturn(verificationCreatedEvent);

        victim.create(event);

        verify(eventService, times(1)).send(verificationCreatedEvent, Topic.VERIFICATION_CREATED);
    }

    private VerificationCreatedEvent prepareCreatedEvent() {
        return VerificationCreatedEvent.builder()
                                       .build();
    }

    private VerificationEntity prepareEntity() {
        return VerificationEntity.builder()
                                 .build();
    }

    private Verification prepareVerification() {
        return Verification.builder()
                           .build();
    }

    private VerificationCreateEvent prepareEvent() {
        return VerificationCreateEvent.builder()
                                      .build();
    }

}