package com.sunfinance.hometask.notification.service;

import com.sunfinance.hometask.api.event.notification.NotificationResult;
import com.sunfinance.hometask.api.event.verification.VerificationCreatedEvent;

import java.util.Optional;

public interface NotificationService {

    Optional<NotificationResult> send(VerificationCreatedEvent event);

}
