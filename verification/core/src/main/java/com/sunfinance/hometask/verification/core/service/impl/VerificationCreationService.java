package com.sunfinance.hometask.verification.core.service.impl;

import com.sunfinance.hometask.api.event.Topic;
import com.sunfinance.hometask.api.event.verification.VerificationCreateEvent;
import com.sunfinance.hometask.api.event.verification.VerificationCreateResult;
import com.sunfinance.hometask.api.event.verification.VerificationCreatedEvent;
import com.sunfinance.hometask.event.EventService;
import com.sunfinance.hometask.verification.core.aggregate.Verification;
import com.sunfinance.hometask.verification.core.aggregate.VerificationFactory;
import com.sunfinance.hometask.verification.core.persistence.VerificationEntity;
import com.sunfinance.hometask.verification.core.persistence.VerificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VerificationCreationService {

    private final Clock clock;
    private final ConversionService conversionService;
    private final VerificationFactory factory;
    private final VerificationRepository repository;
    private final EventService eventService;

    @Transactional
    public Optional<VerificationCreateResult> create(VerificationCreateEvent event) {
        return Optional.of(event)
                       .map(factory::create)
                       .flatMap(this::persistVerification)
                       .flatMap(this::sendEvent)
                       .map(verification -> conversionService.convert(verification, VerificationCreateResult.class));
    }

    private Optional<Verification> persistVerification(Verification verification) {
        return Optional.of(verification)
                       .map(aggregate -> conversionService.convert(aggregate, VerificationEntity.class))
                       .map(this::populateDate)
                       .map(repository::save)
                       .map(entity -> verification);
    }

    private VerificationEntity populateDate(VerificationEntity entity) {
        entity.setCreatedAt(clock.instant());
        return entity;
    }

    private Optional<Verification> sendEvent(Verification verification) {
        return Optional.of(verification)
                       .map(aggregate -> conversionService.convert(aggregate, VerificationCreatedEvent.class))
                       .map(event -> sendEvent(event, verification));
    }

    private Verification sendEvent(VerificationCreatedEvent event, Verification verification) {
        eventService.send(event, Topic.VERIFICATION_CREATED);
        return verification;
    }
}
