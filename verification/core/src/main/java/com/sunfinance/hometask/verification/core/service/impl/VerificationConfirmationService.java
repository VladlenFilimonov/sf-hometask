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
import com.sunfinance.hometask.verification.core.persistence.VerificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VerificationConfirmationService {

    private final VerificationRepository repository;
    private final VerificationFactory verificationFactory;
    private final ConversionService conversionService;
    private final EventService eventService;

    public Optional<VerificationConfirmationResult> confirm(VerificationConfirmationEvent event) {
        return repository.findByVerificationId(event.getVerificationId().toString())
                         .map(verificationFactory::create)
                         .flatMap(verification -> processVerification(verification, event))
                         .map(verification -> conversionService.convert(verification, VerificationConfirmationResult.class));
    }

    private Optional<Verification> processVerification(Verification verification, VerificationConfirmationEvent event) {
        return Optional.of(verification)
                       .filter(aggregate -> compareCode(aggregate, event))
                       .flatMap(this::confirmVerification)
                       .or(() -> incrementAttempt(verification));

    }

    private boolean compareCode(Verification verification, VerificationConfirmationEvent event) {
        return verification.getCode().equals(event.getCode());
    }

    private Optional<Verification> confirmVerification(Verification verification) {
        return Optional.of(verificationFactory.confirm(verification))
                       .flatMap(this::updateConfirmedEntity)
                       .flatMap(this::sendConfirmedEvent);
    }

    private Optional<Verification> sendConfirmedEvent(Verification verification) {
        return Optional.of(verification)
                       .map(aggregate -> conversionService.convert(aggregate, VerificationConfirmedEvent.class))
                       .map(event -> {
                           eventService.send(event, Topic.VERIFICATION_CONFIRMED);
                           return verification;
                       });
    }

    private Optional<Verification> updateConfirmedEntity(Verification verification) {
        return repository.findByVerificationId(verification.getId().toString())
                         .map(entity -> {
                             entity.setConfirmed(verification.isConfirmed());
                             return entity;
                         })
                         .map(repository::save)
                         .map(entity -> verification);
    }

    private Optional<Verification> incrementAttempt(Verification verification) {
        return repository.findByVerificationId(verification.getId().toString())
                         .map(entity -> {
                             entity.setAttempts(entity.getAttempts() + 1);
                             return entity;
                         })
                         .map(repository::save)
                         .flatMap(entity -> sendConfirmationFailedEvent(verification))
                         .flatMap(v -> {
                             throw new InvalidCodeException("Verification code does not much");
                         });
    }

    private Optional<Verification> sendConfirmationFailedEvent(Verification verification) {
        return Optional.of(verification)
                       .map(aggregate -> conversionService.convert(aggregate, VerificationConfirmationFailedEvent.class))
                       .map(event -> {
                           eventService.send(event, Topic.VERIFICATION_CONFIRMATION_FAILED);
                           return verification;
                       });
    }

}
